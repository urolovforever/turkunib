# TURKUNIB yangiliklar paketini saytga import qilish

2026-09-11: `TURKUNIB_web_handover_1.zip` (TIU raisligi doirasida tayyorlangan)
paketidan 29 ta inglizcha maqola + 76 rasm + 6 rukn prod'ga yuklandi.
news 6 → 35 ta bo'ldi.

## Tartib

```bash
# 0) zip ni ochish, rasmlarni yuklab olish (.bat Windows uchun — Linux'da python bilan)
#    keyin siqish: max kenglik 1600px, JPEG q=82  (128 MB -> 12 MB)
python3 parse_articles.py      # article.txt -> articles_parsed.json
python3 build_news.py          # -> import_news.sql + upload_stage/images/<DDMMYYYY>/
tar czf - -C upload_stage images | ssh turkunib 'tar xzf - -C /opt/turkunib/uploads'
ssh turkunib 'chown -R www-data:www-data /opt/turkunib/uploads/images/<DDMMYYYY>'
scp import_news.sql turkunib:/opt/turkunib/ && ssh turkunib 'docker exec -i mariadb-local mariadb -uroot -pPAROL turkunib < ...'
```

## Sayt modeli haqida bilib olinganlar

- Rasm yo'li: `/images/{d}{MM}{yyyy}/` + UUID.jpg — `ImageService.store()` shunday yasaydi.
  Jismoniy joyi `/opt/turkunib/uploads`, URL `/uploads/images/.../uuid.jpg`, egasi www-data.
- `news.image_id` = muqova (show.html da hero sifatida chiqadi, **null bo'lmasligi kerak** —
  `${news.image().url}` null tekshiruvsiz). Qolgan rasmlar `content` ichiga `<img>` bilan.
- `content` Jsoup `Safelist.relaxed()` + iframe + style bilan tozalanadi →
  **`<figure>`/`<figcaption>` YO'Q**, `<a target>` ham yo'q. `<p><img>`, `<small>`, `<em>` bor.
- `news_i18n.locale`: EN/KG/KZ/TR/UZ/HG/AZ. So'rovda **EN fallback** bor — faqat inglizcha
  maqola ham /uz, /tr sahifalarida ko'rinadi (inglizcha matn bilan).
- URL: `/{lang}/news/{id}/{slug}`, slug = Slugify(title).
- `createNews()` created_at ni `now()` qiladi → **sanani orqaga surish faqat DB orqali**.
- Ruknlar EN-only bo'lishi mumkin (mavjud "News" rukni ham shunday).

## 2026-09-11 importida qabul qilingan qarorlar (user)

- 29 tasi ham yuklandi — 2 tasi mavjud yangilik bilan bir voqea haqida (ataylab qoldirildi).
- 6 ta rukn yaratildi (paketdagi "Academic Mobility" hech bir maqolada asosiy rukn emas → yaratilmadi).
- Hammasi darhol ko'rinadigan (enabled=1), `created_at` voqea sanasiga qo'yilgan (2025-01-22 … 2026-08-23).
- Faqat inglizcha; author_name = "TURKUNIB".
- Har rasm ostida majburiy kredit: "Photo: Organization of Turkic States (turkicstates.org)",
  maqola oxirida OTS manba havolasi — paket shartlari shunday.
