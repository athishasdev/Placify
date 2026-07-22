# Placify Production Deployment Guide

This guide details step-by-step instructions for deploying **Placify** to production environments across Render (Backend), Neon (PostgreSQL Database), and Vercel (Frontend).

---

## Architecture Overview

- **Frontend**: Static HTML5/CSS3/JavaScript web application hosted on Vercel.
- **Backend**: Spring Boot 3.4 REST API service running Java 21 hosted on Render.
- **Database**: PostgreSQL server hosted on Neon Database.

---

## Step 1: Provision PostgreSQL Database on Neon

1. Log into [Neon.tech](https://neon.tech) and create a new project named `placify-db`.
2. Select your preferred region.
3. Retrieve your Connection String from the Neon dashboard:
   ```
   postgres://<username>:<password>@ep-example-123456.us-east-2.aws.neon.tech/placify?sslmode=require
   ```
4. Convert this into JDBC format:
   ```
   jdbc:postgresql://ep-example-123456.us-east-2.aws.neon.tech/placify?sslmode=require
   ```

---

## Step 2: Deploy Spring Boot Backend to Render

1. Log into [Render.com](https://render.com) and click **New +** -> **Web Service**.
2. Connect your Git repository.
3. Configure the Web Service properties:
   - **Name**: `placify-api`
   - **Environment**: `Java` (or Docker)
   - **Build Command**: `./mvnw clean package -DskipTests` (or `mvn clean package -DskipTests`)
   - **Start Command**: `java -jar target/placify-backend-1.0.0.jar`
4. Under **Environment Variables**, add:
   | Key | Value |
   |---|---|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `SERVER_PORT` | `8080` |
   | `DATABASE_URL` | `jdbc:postgresql://ep-example-123456.us-east-2.aws.neon.tech/placify?sslmode=require` |
   | `DATABASE_USERNAME` | `<neon_db_username>` |
   | `DATABASE_PASSWORD` | `<neon_db_password>` |
   | `JWT_SECRET` | `<your-secure-base64-secret>` |
   | `UPLOAD_PATH` | `/tmp/uploads` |
   | `ALLOWED_ORIGINS` | `https://placify.vercel.app` |
   | `FRONTEND_URL` | `https://placify.vercel.app` |
5. Deploy service and copy your live backend URL (e.g. `https://placify-api.onrender.com`).

---

## Step 3: Deploy Frontend to Vercel

1. Log into [Vercel.com](https://vercel.com) and click **Add New** -> **Project**.
2. Import the `frontend` directory from your repository.
3. Configure project settings:
   - **Framework Preset**: Other / Static HTML
   - **Root Directory**: `frontend`
4. Add Environment Variable:
   - `ENV_API_BASE_URL` -> `https://placify-api.onrender.com/api`
5. Deploy project.

---

## Step 4: Verification Checklist

- [x] Access `https://placify.vercel.app/pages/login.html`.
- [x] Test demo admin (`admin@placify.com` / `admin123`) login.
- [x] Test demo student (`student@placify.com` / `student123`) login.
- [x] Upload a PDF resume and check skill detection.
- [x] Verify API responses return HTTP 200 without exposing stack traces.
