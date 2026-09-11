import json, sys
rows = json.load(open("import_rows.json"))
SCHOLARSHIP_ID = int(sys.argv[1]) if len(sys.argv) > 1 else 1
AUTHOR_ID      = int(sys.argv[2]) if len(sys.argv) > 2 else 1

def q(v):
    if v is None: return "NULL"
    return "'" + str(v).replace("\\", "\\\\").replace("'", "''") + "'"
def n(v):
    return "NULL" if v is None else str(int(v))

out = [
 "-- Orhun 2026/2027 kvotalari — manba: 'EK 1. Annex 1 Quotas (1).xlsx' (Genel Tablo + 7 batafsil varaq)",
 f"-- scholarship_id={SCHOLARSHIP_ID}, author_id={AUTHOR_ID}, {len(rows)} universitet",
 "START TRANSACTION;",
]
for r in rows:
    out.append(
        "INSERT INTO orhun_quotas (member_id, scholarship_id, student_accept, student_send, "
        "teacher_accept, teacher_send, note, author_id, created_at, updated_at) VALUES ("
        f"{r['member_id']}, {SCHOLARSHIP_ID}, {n(r['student_accept'])}, {n(r['student_send'])}, "
        f"{n(r['teacher_accept'])}, {n(r['teacher_send'])}, {q(r['note'] or None)}, {AUTHOR_ID}, NOW(), NOW())\n"
        "ON DUPLICATE KEY UPDATE student_accept=VALUES(student_accept), student_send=VALUES(student_send), "
        "teacher_accept=VALUES(teacher_accept), teacher_send=VALUES(teacher_send), "
        f"note=VALUES(note), author_id=VALUES(author_id), updated_at=NOW();   -- {r['member'][:50]}"
    )
out.append("COMMIT;")
open("import_quotas.sql", "w", encoding="utf-8").write("\n".join(out) + "\n")
print(f"import_quotas.sql yozildi ({len(rows)} INSERT, scholarship_id={SCHOLARSHIP_ID})")
