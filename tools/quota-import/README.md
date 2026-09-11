# Orhun kvotalarini Excel'dan import qilish

2026-09-11 da `EK 1. Annex 1 Quotas (1).xlsx` faylidan 87 ta universitet kvotasi
`orhun_quotas` jadvaliga (scholarship_id=1) yozilgan. Prod'da bajarilgan.

## Qayta ishlatish tartibi

```bash
# 1. Bazadan member ro'yxatini olish
ssh turkunib 'docker exec mariadb-local mariadb -uroot -pPAROL turkunib -N -B -e \
  "SELECT m.id, COALESCE(c.name,\"\"), m.name FROM members m \
   LEFT JOIN countries c ON c.id=m.country_id WHERE m.enabled=1 ORDER BY m.id;"' > members.tsv

# 2. Nomlarni moslashtirish (natija: cands.json + qo'lda tekshirish ro'yxati)
python3 match2.py

# 3. Qatorlarni qurish (build_import.py ichidagi MANUAL/OVERRIDE/DETAIL ni yangilang)
python3 build_import.py        # -> import_rows.json, import_preview.csv

# 4. SQL (idempotent: ON DUPLICATE KEY UPDATE)
python3 gen_sql.py <scholarship_id> <author_id>   # -> import_quotas.sql
```

## 2026/2027 importida qabul qilingan qarorlar

- `"10/10 (20)"` -> **jami 20** (bir o'quv yili). Taqsimot izohga: `Fall/Spring 10/10`.
- Izohlar **inglizcha** — saytda tarjimasiz chiqadi (orhun-process.html, `q.note`).
- 7 ta universitet ("batafsil jadval N-varaqda") — alohida varaqlar yig'indisi (`DETAIL`).
- Excel ziddiyatlari qo'lda tuzatilgan (`OVERRIDE`): "2/2 (40)"->4, "5/5 (40)"->10.
- Koordinator ism/email/telefoni **ataylab olinmagan** (shaxsiy ma'lumot).
- Yo'nalishlar ro'yxati (81 uni, 5492 belgigacha) **saqlanmagan** — `note` 512 belgi.

## Nomlar moslashuvi

74 ta avtomatik, 13 tasi `build_import.py` ichidagi `MANUAL` lug'atida qo'lda ko'rsatilgan
(masalan "Bishkek State University named after K. Karasayev" -> "Bishkek Humanities University").
Excel nomlari turkcha/inglizcha/mahalliy aralash, shuning uchun har importda tekshirish kerak.
