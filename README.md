# TURKUNIB — Turkic Universities Union

Production website of the Turkic Universities Union, an international association of universities from Turkic states.

**Live:** https://turkunib.org

The site serves public content in **seven languages** (English, Turkish, Uzbek, Kazakh, Kyrgyz, Azerbaijani, Hungarian) and runs the association's day-to-day processes online:

- **Student portal** — registration with e-mail activation, CAPTCHA and login lockout protection, password recovery.
- **Orhun Exchange applications** — two-stage application flow for students and teachers: applicants upload three documents, the home university approves, the host university accepts or rejects.
- **Quota management** — administrators manage exchange quotas through an admin grid; each member university submits its own quota form.
- **Content management** — news, events, announcements, membership pages, governing documents, all editable through an admin panel.

## Stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot, Spring Security (Argon2 password hashing) |
| Views | Thymeleaf server-side templates |
| Database | MariaDB with Liquibase migrations |
| Deployment | Docker (multi-stage build), nginx, Let's Encrypt |

## Configuration

Live credentials are not stored in the repository. Copy the example configs and fill in your values:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
cp src/main/resources/application-docker.properties.example src/main/resources/application-docker.properties
```

## Building

No local JDK required — the project builds inside Docker:

```bash
git clone https://github.com/urolovforever/turkunib.git
cd turkunib
docker build -f Dockerfile.build -t turkunib-build .
# the runnable jar is produced at /app/target/ inside the image
```

Run it against a MariaDB instance:

```bash
docker run --rm -p 8080:8080 turkunib-build java -jar /app/target/turk-0.0.1-SNAPSHOT.jar
```

## Screenshots

_to be added_
