# PostgreSQL Setup & Migration Guide

This document covers local and cloud PostgreSQL setup for **Placify**.

---

## 1. Local PostgreSQL Setup

### Prerequisites
- Install PostgreSQL 14+ on your workstation.
- Install `pgAdmin` or use `psql` CLI.

### Creating Database
Run the following SQL commands in `psql` or pgAdmin Query Tool:

```sql
CREATE DATABASE placify;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE placify TO postgres;
```

---

## 2. Spring Boot Properties Configuration

In `backend/src/main/resources/application-local.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/placify
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 3. Cloud Database Setup (Neon PostgreSQL)

1. Sign up on [Neon Database](https://neon.tech).
2. Create a new PostgreSQL database instance.
3. Obtain the connection string:
   `postgres://username:password@ep-host.neon.tech/placify?sslmode=require`
4. Set environment variables on your deployment server:
   - `DATABASE_URL` = `jdbc:postgresql://ep-host.neon.tech/placify?sslmode=require`
   - `DATABASE_USERNAME` = `username`
   - `DATABASE_PASSWORD` = `password`

---

## 4. Schema Auto-Generation

Spring Data JPA will automatically initialize database tables:
- `users`
- `resumes`
- `job_roles`
- `skills`
- `analysis_reports`
- Join tables for `job_role_skills`, `resume_skills`, `report_missing_skills`

Seed data (skills, job roles, default demo accounts) is automatically inserted by `DataInitializer.java` on initial startup.
