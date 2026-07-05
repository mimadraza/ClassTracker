# ClassTracker

Multi-tenant monthly class & payment tracker for tutors. Log classes through the
month, confirm the ones actually delivered, see a live confirmed total, then lock
the month and export a week-by-week summary (CSV or print/PDF) to hand to your
employer.

## Stack

- **Backend**: Spring Boot 3 (Java 17), Spring Security + JWT, JPA/Hibernate
- **Database**: PostgreSQL (Supabase) in production, embedded H2 for local dev
- **Frontend**: React 18 + Vite (`frontend/`), mobile-friendly, calendar month view

## Run locally (no external database needed)

Terminal 1 — backend on :8080 with an embedded H2 database (persisted to `./data/`):

```
mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Terminal 2 — frontend on :5173 (proxies `/api` to :8080):

```
cd frontend
npm install
npm run dev
```

Open http://localhost:5173, create an account, add a class type, and start logging.

## Run against Supabase/Postgres

The old direct-connection host in this repo (`db.kdznuteggmfxmxprbwva.supabase.co`)
no longer resolves. Get a fresh connection string from your Supabase dashboard
(Project Settings → Database → Connection string, use the **session pooler** URI),
then run without the `local` profile:

```
set DB_URL=jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres
set DB_USER=postgres.<project-ref>
set DB_PASSWORD=<your-password>
set JWT_SECRET=<random string, 32+ chars>
mvnw spring-boot:run
```

Tables are created automatically (`spring.jpa.hibernate.ddl-auto=update`).

> **Security note**: a database password was previously committed to this repo's
> git history. Rotate it in Supabase and never rely on the defaults in
> `application.properties` for production.

## API overview

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/auth/signup` / `/api/auth/login` | Returns `{ token, user }`; send `Authorization: Bearer <token>` after |
| GET | `/api/me` | Current user |
| GET/POST | `/api/class-types` · PUT/DELETE `/api/class-types/{id}` | Class types with default rate + currency |
| POST | `/api/classes` · PUT/DELETE `/api/classes/{id}` | Log/edit/delete a class (rate defaults from its type) |
| PATCH | `/api/classes/{id}/confirm` | Body `{"confirmed": true|false}` — only confirmed classes count |
| GET | `/api/summary?year=&month=` | Week-by-week breakdown + confirmed totals + lock status |
| POST | `/api/summary/lock` / `unlock` `?year=&month=` | Locking blocks all edits in that month |
| GET | `/api/summary/export.csv?year=&month=` | CSV export (week subtotals + grand total) |

All data is scoped to the authenticated user — every tutor only ever sees their own
class types, entries, and locks.

## Frontend build

```
cd frontend
npm run build   # outputs frontend/dist
```

“Print / PDF” on the dashboard prints a clean employer-ready summary via the
browser's print-to-PDF (`@media print` styles).
