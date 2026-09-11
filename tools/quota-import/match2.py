import openpyxl, re, unicodedata, difflib, json

TR = str.maketrans({"ı":"i","İ":"i","ğ":"g","Ğ":"g","ü":"u","Ü":"u","ş":"s","Ş":"s",
                    "ö":"o","Ö":"o","ç":"c","Ç":"c","ə":"a","Ə":"a","ń":"n","ǵ":"g",
                    "ʻ":"","ʼ":"","‘":"","’":"","´":"","`":""})
STOP = {"university","universitesi","universiteti","universitet","universiteta","universities",
        "unversitesi","universite","univ","of","the","and","ve","named","after","adina","adyndagy",
        "atindagi","nomidagi","adına","institute","enstitusu","instituti","academy","akademiyasi",
        "akademisi","higher","school","name","dan","adi","named"}
# these are meaningful, keep them: state/national/technical/pedagogical...

def norm_tokens(s):
    if s is None: return []
    s = str(s).translate(TR)
    s = unicodedata.normalize("NFKD", s)
    s = "".join(c for c in s if not unicodedata.combining(c)).lower()
    s = re.sub(r"[^a-z0-9]+", " ", s)
    return [t for t in s.split() if t and t not in STOP and len(t) > 1]

def variants(name):
    parts = re.split(r"[/|\n]", str(name))
    out = [str(name)] + [p for p in parts if len(p.strip()) > 4]
    return out

def score(a_toks, b_toks):
    if not a_toks or not b_toks: return 0.0
    A, B = set(a_toks), set(b_toks)
    inter = A & B
    # fuzzy token overlap: a token also counts if a close variant exists
    extra = 0
    for x in A - inter:
        for y in B - inter:
            if difflib.SequenceMatcher(None, x, y).ratio() >= 0.86:
                extra += 1; break
    ov = (len(inter) + extra) / min(len(A), len(B))          # coverage of the shorter name
    jac = (len(inter) + extra) / len(A | B)                  # overall similarity
    seq = difflib.SequenceMatcher(None, " ".join(sorted(A)), " ".join(sorted(B))).ratio()
    return round(0.5*ov + 0.3*jac + 0.2*seq, 4)

members = []
for line in open("members.tsv", encoding="utf-8"):
    mid, country, mname = line.rstrip("\n").split("\t")
    members.append({"id": int(mid), "country": country, "name": mname, "toks": norm_tokens(mname)})

CMAP = {"Azerbaycan":"Azerbaijan","Kazakistan":"Kazakhstan","Özbekistan":"Uzbekistan",
        "Kırgızistan":"Kyrgyzstan","Türkiye":"Turkiye","Macaristan":"Hungary",
        "KKTC":"Turkish Republic of Northern Cyprus"}

wb = openpyxl.load_workbook("/home/tiu/Downloads/Telegram Desktop/EK 1. Annex 1 Quotas (1).xlsx", data_only=True)
ws = wb["Genel Tablo"]
out = []
for i, r in enumerate(ws.iter_rows(min_row=3, max_row=90, max_col=13, values_only=True), start=3):
    if not r[2] or not str(r[2]).strip(): continue
    country = CMAP.get(str(r[0]).strip() if r[0] else "", "")
    cands = []
    for m in members:
        best = max(score(norm_tokens(v), m["toks"]) for v in variants(r[2]))
        if country and m["country"] != country:
            best -= 0.30                      # cross-country match is a strong negative
        cands.append((best, m))
    cands.sort(key=lambda x: -x[0])
    out.append({"row": i, "country": country, "uni": re.sub(r"\s+"," ",str(r[2])).strip(),
                "cands": [{"score": round(s,3), "id": m["id"], "name": m["name"], "country": m["country"]}
                          for s, m in cands[:3]]})
json.dump(out, open("cands.json","w"), ensure_ascii=False, indent=1)

auto  = [o for o in out if o["cands"][0]["score"] >= 0.80 and o["cands"][0]["score"] - o["cands"][1]["score"] >= 0.12]
rest  = [o for o in out if o not in auto]
print(f"Jami qator: {len(out)}   |  avtomatik ishonchli: {len(auto)}   |  qo'lda tekshirish: {len(rest)}")
ids = {}
for o in auto: ids.setdefault(o["cands"][0]["id"], []).append(o["row"])
print("auto dublikat:", {k:v for k,v in ids.items() if len(v)>1} or "yo'q")
print("\n===== QO'LDA TEKSHIRISH KERAK =====")
for o in rest:
    print(f"\nr{o['row']} [{o['country']}] {o['uni'][:85]}")
    for c in o["cands"]:
        print(f"     {c['score']:<6} #{c['id']:<4} [{c['country'][:12]:<12}] {c['name'][:62]}")
