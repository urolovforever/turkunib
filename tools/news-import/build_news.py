# -*- coding: utf-8 -*-
"""29 maqola + 76 rasm -> news/news_i18n/images/news_categories uchun SQL + yuklanadigan fayllar."""
import json, os, re, uuid, hashlib, shutil, html, datetime, unicodedata

ARTS      = json.load(open("articles_parsed.json"))
UPLOAD_D  = datetime.date.today()
IMG_PATH  = f"/images/{UPLOAD_D.day}{UPLOAD_D.month:02d}{UPLOAD_D.year}/"
STAGE     = "upload_stage" + IMG_PATH            # nusxalash uchun mahalliy papka
AUTHOR_ID = 1
AUTHOR_NM = "TURKUNIB"
CREDIT    = "Photo: Organization of Turkic States (turkicstates.org)"
shutil.rmtree("upload_stage", ignore_errors=True)
os.makedirs(STAGE, exist_ok=True)

def slugify(s):
    s = unicodedata.normalize("NFKD", s)
    s = "".join(c for c in s if not unicodedata.combining(c))
    s = s.replace("ı","i").replace("ğ","g").replace("ş","s").replace("ö","o").replace("ü","u").replace("ç","c")
    s = re.sub(r"[’'‘’]", "", s.lower())
    s = re.sub(r"[^a-z0-9]+", "-", s).strip("-")
    return re.sub(r"-{2,}", "-", s)[:200]

def sq(v):
    if v is None: return "NULL"
    return "'" + str(v).replace("\\", "\\\\").replace("'", "''") + "'"

def esc(t):   # matn -> HTML (tirnoq/tire saqlanadi)
    return html.escape(t, quote=False)

# ---- 1. Ruknlar (faqat EN — saytdagi mavjud "News" rukni ham EN-only) ----
cats = []
seen = set()
for a in ARTS:
    if a["category"] not in seen:
        seen.add(a["category"]); cats.append(a["category"])
cat_slug = {c: slugify(c) for c in cats}

# ---- 2. Rasmlar: UUID nom + md5, staging papkaga nusxa ----
images = []     # (uuid_name, orig_name, size, md5, local_src)
for a in ARTS:
    a["img_rows"] = []
    for src in a["photos"]:
        data = open(src, "rb").read()
        name = f"{uuid.uuid4()}.jpg"
        shutil.copyfile(src, os.path.join(STAGE, name))
        row = {"file": name, "orig": os.path.basename(src),
               "size": len(data), "md5": hashlib.md5(data).hexdigest()}
        images.append(row); a["img_rows"].append(row)

# ---- 3. Har maqola uchun content HTML ----
for a in ARTS:
    parts = [f"<p>{esc(p)}</p>" for p in a["paras"]]
    for row in a["img_rows"][1:]:           # 1-rasm muqova, qolgani matn ichida
        parts.append(f'<p><img src="/uploads{IMG_PATH}{row["file"]}" '
                     f'alt="{html.escape(a["headline"], quote=True)}" '
                     f'style="max-width:100%;height:auto;"></p>')
        parts.append(f"<p><small>{CREDIT}</small></p>")
    if a["img_rows"]:
        parts.insert(len(a["paras"]), f"<p><small>{CREDIT}</small></p>")   # muqova krediti
    if a["src_url"]:
        label = esc(a["src_name"]) if a["src_name"] else "OTS"
        parts.append(f'<p><em>Based on official OTS reporting: '
                     f'<a href="{html.escape(a["src_url"], quote=True)}">{label}</a></em></p>')
    a["content"] = "\n".join(parts)
    d = a["paras"][0]
    a["description"] = (d[:297].rsplit(" ", 1)[0] + "…") if len(d) > 300 else d
    a["slug"] = slugify(a["headline"])

# ---- 4. SQL ----
L = ["-- TURKUNIB news paketi: 29 maqola + 76 rasm + 6 rukn",
     f"-- manba: TURKUNIB_web_handover_1.zip   rasm yo'li: {IMG_PATH}",
     "START TRANSACTION;", ""]
L.append("-- === RUKNLAR ===")
for c in cats:
    L.append(f"INSERT INTO news_categories (created_at, updated_at) VALUES (NOW(), NOW());")
    L.append(f"SET @cid = LAST_INSERT_ID();")
    L.append(f"INSERT INTO news_categories_i18n (category_id, locale, title, slug, description, created_at, updated_at) "
             f"VALUES (@cid, 'EN', {sq(c)}, {sq(cat_slug[c])}, NULL, NOW(), NOW());")
L.append("")
L.append("-- === RASMLAR ===")
for r in images:
    L.append("INSERT INTO images (image_name, image_path, image_file_name, image_type, image_content_type, "
             f"image_size, md5_hash, created_by_module, author_id, created_at, updated_at) VALUES ("
             f"{sq(r['orig'])}, {sq(IMG_PATH)}, {sq(r['file'])}, '.jpg', 'image/jpeg', "
             f"{r['size']}, {sq(r['md5'])}, 'news', {AUTHOR_ID}, NOW(), NOW());")
L.append("")
L.append("-- === YANGILIKLAR ===")
for a in sorted(ARTS, key=lambda x: x["date"]):      # eskisidan yangisiga
    ts = f"{a['date']} 09:00:00"
    L.append(f"-- #{a['no']} [{a['priority']}] {a['date']}  {a['headline'][:70]}")
    L.append("INSERT INTO news (image_id, category_id, author_name, enabled, author_id, created_at, updated_at) VALUES ("
             f"(SELECT id FROM images WHERE image_file_name={sq(a['img_rows'][0]['file'])}), "
             f"(SELECT c.id FROM news_categories c JOIN news_categories_i18n ci ON ci.category_id=c.id "
             f"AND ci.locale='EN' WHERE ci.slug={sq(cat_slug[a['category']])}), "
             f"{sq(AUTHOR_NM)}, 1, {AUTHOR_ID}, {sq(ts)}, {sq(ts)});")
    L.append("SET @nid = LAST_INSERT_ID();")
    L.append("INSERT INTO news_i18n (news_id, locale, title, slug, content, description, created_at, updated_at) VALUES ("
             f"@nid, 'EN', {sq(a['headline'])}, {sq(a['slug'])}, {sq(a['content'])}, {sq(a['description'])}, {sq(ts)}, {sq(ts)});")
L.append("COMMIT;")
open("import_news.sql", "w", encoding="utf-8").write("\n".join(L) + "\n")

json.dump(ARTS, open("articles_built.json", "w"), ensure_ascii=False, indent=1)
print(f"import_news.sql: {len(L)} qator")
print(f"ruknlar: {len(cats)} — {', '.join(cats)}")
print(f"rasmlar: {len(images)} ta -> upload_stage{IMG_PATH}")
print(f"staging hajmi: {sum(os.path.getsize(os.path.join(STAGE,f)) for f in os.listdir(STAGE))/1048576:.1f} MB")
print(f"eng uzun content: {max(len(a['content']) for a in ARTS)} belgi;  description max: {max(len(a['description']) for a in ARTS)}")
