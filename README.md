# Placify — Student Placement Skill Gap Analyzer

A full-stack web application that helps students identify missing skills for their target job roles and generates a personalized learning roadmap.

---

## 🛠️ Tech Stack

| Layer      | Technology |
|------------|------------|
| Frontend   | HTML5, CSS3, Vanilla JavaScript |
| Backend    | Java 21, Spring Boot 3.4.3, Spring MVC, Spring Security |
| ORM        | Spring Data JPA + Hibernate |
| Database   | PostgreSQL |
| PDF Parser | Apache PDFBox 3.0.4 |
| Build Tool | Maven |

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 21+
- Maven 3.8+
- PostgreSQL 14+
- A modern browser (Chrome, Firefox, Edge)

---

### 1. Database Setup (PostgreSQL)

Open pgAdmin or psql and run:

```sql
CREATE DATABASE placify;
```

The schema will be created automatically by Hibernate (`ddl-auto=update`).
Default seed data (admin user, skills, job roles) is loaded on first startup via `DataInitializer`.

---

### 2. Configure Backend

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/placify
spring.datasource.username=postgres
spring.datasource.password=postgres    ← Change this
```

---

### 3. Run the Backend

```bash
cd "backend"
mvn spring-boot:run
```

Backend will start at: **http://localhost:8080**

---

### 4. Run the Frontend

Open the frontend with Live Server (VS Code extension) or any static file server:

- **VS Code Live Server**: Right-click `frontend/index.html` → Open with Live Server
- **Python**: `python -m http.server 5500` inside the `frontend/` folder
- **Direct file**: Open `frontend/index.html` in a browser (note: CORS may block API calls)

Frontend runs at: **http://localhost:5500**

---

## 🔐 Demo Accounts

| Role    | Email                    | Password    |
|---------|--------------------------|-------------|
| Student | student@placify.com      | student123  |
| Admin   | admin@placify.com        | admin123    |

---

## 📁 Project Structure

```
Job identifier/
├── backend/                          # Spring Boot Backend
│   ├── pom.xml
│   └── src/main/java/com/placify/
│       ├── config/                   # Security, CORS, DataInitializer
│       ├── controller/               # REST API controllers
│       ├── dto/                      # Request/Response DTOs
│       ├── exception/                # Global exception handling
│       ├── model/                    # JPA entities
│       ├── repository/               # Spring Data JPA repos
│       └── service/                  # Business logic
├── frontend/                         # Vanilla JS Frontend
│   ├── index.html                    # Landing page
│   ├── css/styles.css                # Design system
│   ├── js/api.js                     # API client
│   ├── js/utils.js                   # Shared utilities
│   └── pages/
│       ├── login.html
│       ├── register.html
│       ├── dashboard.html            # Student dashboard
│       ├── upload.html               # Resume upload
│       ├── analysis.html             # Skill gap analysis
│       └── admin.html                # Admin panel
└── database/
    └── schema.sql                    # PostgreSQL schema reference
```

---

## 🌐 REST API Reference

| Method | Endpoint              | Description              | Auth         |
|--------|-----------------------|--------------------------|--------------|
| POST   | /api/auth/register    | Student registration     | Public       |
| POST   | /api/auth/login       | Login                    | Public       |
| POST   | /api/auth/logout      | Logout                   | Authenticated|
| GET    | /api/auth/me          | Current user info        | Authenticated|
| POST   | /api/resume/upload    | Upload PDF resume        | Student      |
| GET    | /api/resume/latest    | Get latest resume        | Student      |
| GET    | /api/resume/{id}      | Get resume by ID         | Student      |
| GET    | /api/roles            | List job roles           | Authenticated|
| POST   | /api/roles            | Create job role          | Admin        |
| PUT    | /api/roles/{id}       | Update job role          | Admin        |
| DELETE | /api/roles/{id}       | Delete job role          | Admin        |
| POST   | /api/analyze          | Run skill gap analysis   | Student      |
| GET    | /api/report/{id}      | Get analysis report      | Authenticated|
| GET    | /api/reports          | Get my reports           | Student      |
| GET    | /api/dashboard/student| Student dashboard data   | Student      |
| GET    | /api/dashboard/admin  | Admin dashboard data     | Admin        |

---

## ✨ Features

- **Resume Upload**: Drag-and-drop PDF upload with PDFBox text extraction
- **Skill Detection**: Rule-based parser with 29+ skills and alias matching
- **Skill Gap Analysis**: Match score formula: `(Matched / Total Required) × 100`
- **Learning Roadmap**: Personalized improvement plan with curated resource links
- **6 Job Roles**: Java Backend, Full Stack, Software Engineer, QA, Frontend, DevOps
- **Admin Panel**: Manage job roles with full CRUD and platform statistics
- **Student Dashboard**: Resume, detected skills, past reports, and average match score

---

## 🔮 Future Enhancements

- JWT-based stateless authentication
- NLP-based semantic skill extraction
- ATS resume scoring
- Company-specific role matching
- Email report delivery
- Cloud deployment (AWS/GCP)
