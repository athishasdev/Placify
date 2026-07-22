# Placify - Enterprise Placement Skill Gap Analyzer

Placify is a full-stack student placement skill gap analysis platform built with **Spring Boot 3.4** and modern **HTML5/CSS3/JavaScript**. It parses student PDF resumes, extracts technical competencies, compares them against target job roles, computes match scores, and generates actionable, personalized learning roadmaps.

---

## 🌟 Modern Human-Designed UI

Placify features a sleek, professional SaaS interface inspired by Linear, Vercel, Stripe, GitHub, and Notion:
- **Zero AI-glowing artifacts**: No neon glows, no random floating blobs, no glassmorphism clutter.
- **Strict 8px grid system**: Uniform spacing, padding, and layout boundaries.
- **Accessible Typography**: High-contrast, clean font hierarchy using Google Inter.
- **Single Accent Color System**: Refined Slate/Zinc dark baseline paired with Royal Blue accent (`#2563eb`).
- **Responsive Layout**: Designed for mobile, tablet, laptop, and high-DPI desktop displays.

---

## 🚀 Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.4.3** (Web, Data JPA, Security, Validation)
- **PostgreSQL** Database Driver & Hibernate ORM
- **Apache PDFBox 3.0** (PDF Parsing & Skill Extraction Engine)
- **JJWT 0.12** (Stateless Token Authentication)
- **Lombok** & **Logback** (Structured File Logging)

### Frontend
- **HTML5** & **Vanilla CSS3** (Custom Design Tokens)
- **Modern ES6 JavaScript** (`async/await`, Fetch API)
- Parameterized Central Config (`config.js`)

---

## 📁 Folder Structure

```
Placify/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/placify/
│   │       │   ├── config/          # Security, JWT, CORS, DataInitializer
│   │       │   ├── controller/      # REST API Endpoints
│   │       │   ├── dto/             # Request & Response DTOs
│   │       │   ├── exception/       # Global Exception Handler
│   │       │   ├── model/           # JPA Entities & Enums
│   │       │   ├── repository/      # Spring Data Repositories
│   │       │   └── service/         # Business Logic & PDF Processing
│   │       └── resources/
│   │           ├── application.properties        # Profile Selector
│   │           ├── application-local.properties  # Local Dev Config
│   │           ├── application-prod.properties   # Production Config
│   │           └── logback-spring.xml            # Structured File Logging
│   ├── pom.xml
│   └── uploads/                     # Resume Storage Directory
├── frontend/
│   ├── css/
│   │   └── styles.css               # Unified Design System
│   ├── js/
│   │   ├── config.js                # Environment API Base URL Config
│   │   ├── api.js                   # API Client Wrapper
│   │   └── utils.js                 # UI & Toast Utilities
│   ├── pages/
│   │   ├── admin.html               # Admin Dashboard & Role Management
│   │   ├── analysis.html            # Skill Gap Analysis & Roadmap
│   │   ├── dashboard.html           # Student Analytics Dashboard
│   │   ├── login.html               # Authentication Portal
│   │   ├── register.html            # Account Creation
│   │   └── upload.html             # PDF Resume Upload Dropzone
│   └── index.html                   # Public Landing Page
├── docs/
│   ├── deployment_guide.md          # Render, Neon & Vercel Guide
│   └── postgresql_setup.md          # Database Installation Guide
├── logs/                            # Application & Error Log Files
├── .env.example                     # Environment Variables Template
├── .gitignore                       # Production Git Exclusions
└── README.md                        # Documentation
```

---

## 🔑 Centralized Demo Credentials

| Role | Email | Password | Access Level |
|---|---|---|---|
| **Admin** | `admin@placify.com` | `admin123` | Role creation, system metrics, skill management |
| **Student** | `student@placify.com` | `student123` | Resume upload, gap analysis, roadmaps |

---

## ⚙️ Environment Variables Matrix

Documented in [.env.example](file:///.env.example):

| Variable | Default (Local) | Description |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `local` | Active Spring profile (`local` or `prod`) |
| `SERVER_PORT` | `8080` | Spring Boot HTTP port |
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/placify` | PostgreSQL connection URL |
| `DATABASE_USERNAME` | `postgres` | Database user |
| `DATABASE_PASSWORD` | `postgres` | Database password |
| `JWT_SECRET` | `WjNkbGR...` | Base64 encoded 256-bit secret key |
| `UPLOAD_PATH` | `uploads/resumes` | Directory path for stored resumes |
| `FRONTEND_URL` | `http://localhost:5500` | Frontend web client URL |
| `ALLOWED_ORIGINS` | `http://localhost:5500,http://localhost:3000` | Comma-separated CORS origins |
| `API_BASE_URL` | `http://localhost:8080/api` | Frontend API endpoint target |
| `DEMO_ADMIN_EMAIL` | `admin@placify.com` | Centralized admin email |
| `DEMO_ADMIN_PASSWORD` | `admin123` | Centralized admin password |
| `DEMO_STUDENT_EMAIL` | `student@placify.com` | Centralized student email |
| `DEMO_STUDENT_PASSWORD` | `student123` | Centralized student password |

---

## 🛠️ Local Setup & Quickstart

### 1. Database Setup
Create PostgreSQL database named `placify`:
```sql
CREATE DATABASE placify;
```

### 2. Backend Execution
Navigate to `backend` directory and build/run:
```bash
cd backend
mvn clean package -DskipTests
mvn spring-boot:run
```
The backend API will start on **`http://localhost:8080/api`**.

### 3. Frontend Execution
Serve the `frontend` folder using any local HTTP server (VS Code Live Server, python http.server, etc.):
```bash
# Example using Python http.server
cd frontend
python -m http.server 5500
```
Open **`http://localhost:5500`** in your browser.

---

## 🌐 Production Deployment

Refer to [docs/deployment_guide.md](file:///docs/deployment_guide.md) for step-by-step instructions on:
1. Provisioning PostgreSQL on **Neon**.
2. Deploying Java backend to **Render**.
3. Deploying static frontend to **Vercel**.

---

## 🔒 Security Features

- **Path Traversal Protection**: Uploaded files are sanitized and validated to prevent directory traversal attacks (`../`).
- **File Validation**: Strict PDF MIME type check and 5MB size limit.
- **BCrypt Hashing**: Password storage secured using Spring Security BCrypt encoder.
- **Externalized JWT**: Secret key externalized to environment properties.
- **Sanitized Error Responses**: Internal stack traces are suppressed in production mode (`server.error.include-stacktrace=never`).

---

## ❓ Troubleshooting & Common Issues

1. **CORS Errors**:
   Ensure `ALLOWED_ORIGINS` on the backend matches your exact frontend URL (including port and protocol).
2. **Database Connection Failure**:
   Check PostgreSQL service status (`pg_isready`) and verify credentials in `application-local.properties` or environment variables.
3. **File Upload Errors**:
   Verify that `UPLOAD_PATH` directory has write permissions.

---

© 2026 Placify. Enterprise Placement Analytics.
