# -*- coding: utf-8 -*-
"""TURKUNIB_web_handover paketidagi 29 maqolani news/news_i18n uchun tayyorlaydi."""
import re, os, glob, json, datetime, html

BASE = "hz/TURKUNIB_web_handover"
MONTHS = {m: i for i, m in enumerate(
    ["January","February","March","April","May","June","July",
     "August","September","October","November","December"], 1)}

def parse_date(s):
    """'8 June 2026' / '6–15 July 2026' / '27–28 April 2026' -> date (boshlanish kuni)."""
    s = s.replace("–", "-").replace("—", "-").strip()
    m = re.search(r"(\d{1,2})(?:\s*-\s*\d{1,2})?\s+([A-Za-z]+)\s+(\d{4})", s)
    if not m: raise ValueError("sana o'qilmadi: " + s)
    return datetime.date(int(m.group(3)), MONTHS[m.group(2)], int(m.group(1)))

def field(txt, name):
    m = re.search(rf"^{name}:\s*(.+?)$", txt, re.M)
    return m.group(1).strip() if m else ""

arts = []
for folder in sorted(glob.glob(f"{BASE}/articles/*/")):
    t = open(os.path.join(folder, "article.txt"), encoding="utf-8").read()
    head = t.split("------------------------------- BODY")[0]
    body = t.split("BODY ------------------")[-1].split("--------------------------- PHOTOGRAPHS")[0]
    tail = t.split("----------------------------- SOURCE")[-1]

    headline = ""
    m = re.search(r"HEADLINE:\s*\n(.+?)\n\s*\n", head, re.S)
    if m: headline = re.sub(r"\s+", " ", m.group(1)).strip()

    paras = [re.sub(r"\s+", " ", p).strip() for p in body.split("\n\n")]
    paras = [p for p in paras if len(p) > 40 and not p.startswith("---")]

    src_name, src_url = "", ""
    m = re.search(r"Original OTS source page\(s\):\s*\n\s*(.+?)\s*\n\s*(https?://\S+)", tail)
    if m: src_name, src_url = m.group(1).strip(), m.group(2).strip()
    else:
        m2 = re.search(r"(https?://\S+)", tail)
        if m2: src_url = m2.group(1).strip()
        m3 = re.search(r"page\(s\):\s*\n\s*(.+)", tail)
        if m3: src_name = m3.group(1).strip()

    photos = sorted(glob.glob(os.path.join(folder, "photos", "*.jpg")))
    cat_raw = field(head, "WEBSITE CATEGORY")
    arts.append({
        "folder": os.path.basename(folder.rstrip("/")),
        "no": int(os.path.basename(folder.rstrip("/")).split("_")[0]),
        "priority": os.path.basename(folder.rstrip("/")).split("_")[1],
        "headline": headline,
        "date": parse_date(field(head, "DATE OF EVENT")).isoformat(),
        "date_raw": field(head, "DATE OF EVENT"),
        "location": field(head, "LOCATION"),
        "category_raw": cat_raw,
        "category": cat_raw.split("/")[0].strip(),
        "tags": field(head, "TAGS/KEYWORDS"),
        "paras": paras,
        "photos": photos,
        "src_name": src_name, "src_url": src_url,
    })

json.dump(arts, open("articles_parsed.json", "w"), ensure_ascii=False, indent=1)
print(f"{len(arts)} ta maqola o'qildi\n")
bad = [a for a in arts if not a["headline"] or len(a["paras"]) < 2 or not a["src_url"] or not a["photos"]]
print("MUAMMOLI:", [(a["no"], not a["headline"], len(a["paras"]), bool(a["src_url"]), len(a["photos"])) for a in bad] or "yo'q")
from collections import Counter
print("\nRuknlar (birinchi qism):")
for c, n in Counter(a["category"] for a in arts).most_common(): print(f"   {n:>2} × {c}")
print("\nSanalar:", min(a["date"] for a in arts), "—", max(a["date"] for a in arts))
print("Rasmlar:", sum(len(a['photos']) for a in arts), "ta;  abzatslar: o'rtacha", round(sum(len(a['paras']) for a in arts)/29,1))
