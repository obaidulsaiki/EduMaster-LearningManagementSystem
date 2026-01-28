# 🎓 EduMaster — Learning Management System

[![Status](https://img.shields.io/badge/status-active-brightgreen)]() [![License](https://img.shields.io/badge/license-MIT-blue)]()

A modern, full-stack Learning Management System (LMS) built to manage courses, lectures, users, profiles, certificates and invoices. EduMaster combines a Java Spring backend with a React + Vite frontend to provide a modular, extensible platform for educators and learners.

> Built for clarity, extensibility, and real-world LMS workflows — enroll students, publish lectures, track progress, issue certificates, and generate invoices.

---

## 🚀 Highlights — What EduMaster Does (Core Functionalities)

- User management & roles
  - Student, Teacher, Admin roles with role-based behavior
- Profile management
  - Students and teachers can manage profiles and upload education & experience
- Education history
  - Add/update/delete education entries (SSC, HSC, Bachelor, Master, PhD)
- Course & lecture management
  - Teachers can create courses, add ordered lectures (video URLs), and manage content
- Enrollment & progress tracking
  - Track completed courses and downloadable certificates
- Certificate & invoice generation
  - Generate and download student certificates and purchase invoices as PDFs
- Admin dashboard & analytics
  - Admins view stats (teachers, students, courses, revenue) and inspect teacher profiles
- RESTful API & frontend separation
  - Clear DTOs, services, and controllers on the backend; modular React components on the frontend

---

## 🏗 Architecture Overview

- Backend: lms/
  - Java Spring Boot application
  - JPA entities (e.g., Education, Student), DTOs (EducationDTO, LectureResponseDTO)
  - Services (EducationService, InvoiceService) and PDF invoice generation
- Frontend: lms-frontend/
  - React + Vite single-page app
  - Components and pages: Profile, AdminDashboard, AdminTeacherView, EducationModal, LectureModal, etc.
- Designed for local development or containerized deployment (Docker/Compose recommended)

---

## 🧭 Folder Map (Important files & folders)

- lms/
  - src/main/java/com/example/lms/entity — Entities (Education, EducationType, Student, ...)
  - src/main/java/com/example/lms/dto — DTOs
  - src/main/java/com/example/lms/service — Business logic (EducationService, InvoiceService)
  - src/main/resources — application.properties / YAML
- lms-frontend/
  - src/pages — Route pages (Profile, Admin)
  - src/components — Reusable UI components & modals (EducationModal, LectureModal)
  - package.json, vite config, index.html

---

## 🔧 Tech Stack

- Backend: Java, Spring Boot, Spring Data JPA, Lombok
- Frontend: React, Vite
- PDF generation: com.lowagie / iText-style library used for invoices
- DB: Any relational DB supported by Spring (Postgres, MySQL) or H2 for development
- Build: Maven/Gradle (backend), npm/yarn/pnpm (frontend)

---

## 💻 Quick Start (Development)

Prerequisites:
- Java 17+ (or required project version)
- Maven or Gradle (or use project wrapper)
- Node.js 18+
- A running database (Postgres/MySQL) or H2 for local dev

Backend
```bash
# from repo root or lms/
# If using Maven wrapper:
./mvnw spring-boot:run

# Or with Maven:
mvn spring-boot:run

# Build jar
mvn package
java -jar lms/target/*.jar
```

Frontend
```bash
cd lms-frontend
# install deps
npm install
# dev server
npm run dev
# production build
npm run build
```

Tip: Ensure frontend API base URL points to backend (VITE_API_BASE_URL or config in API utilities).

---

## ⚙️ Config / Environment Variables (Suggested)

Backend (application.properties / environment):
- SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/edumaster
- SPRING_DATASOURCE_USERNAME=your_user
- SPRING_DATASOURCE_PASSWORD=your_password
- SPRING_JPA_HIBERNATE_DDL_AUTO=update
- JWT_SECRET=your_jwt_secret (if JWT is used)
- SERVER_PORT=8080

Frontend (.env or env.local):
- VITE_API_BASE_URL=http://localhost:8080/api

(Adjust names if the project uses different property keys — add `.env.example` to the repo to document exact keys.)

---

## 🐳 Optional: Docker Compose (example snippet)
A simple docker-compose can spin DB + backend + frontend (sketch):

```yaml
version: "3.8"
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: edumaster
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"

  backend:
    build: ./lms
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/edumaster
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
    depends_on:
      - db
    ports:
      - "8080:8080"

  frontend:
    build: ./lms-frontend
    ports:
      - "5173:5173"
    environment:
      VITE_API_BASE_URL: "http://backend:8080/api"
    depends_on:
      - backend
```

(Adjust Dockerfiles and build contexts as needed — I can add Dockerfiles if you want.)

---

## ✅ Testing & Quality

- Backend tests: `mvn test`
- Frontend tests: `npm test` (if test scripts / setup exist)
- Recommend adding GitHub Actions for CI: lint, test, build backend, run frontend build

---

## 🤝 Contributing

We welcome contributions! Suggested starter tasks:
- Add `.env.example` & `README` improvements
- Provide a Docker Compose setup and Dockerfiles
- Add API docs (Swagger/OpenAPI) and example requests
- Improve UI/UX for modals and forms and add validation & tests

Please open an issue for discussion or submit a PR with a clear description of changes.

---

## 📝 Roadmap (short)
- [ ] Add Docker + docker-compose for one-command dev setup
- [ ] Seed data & sample users for quick demos
- [ ] API reference (Swagger)
- [ ] E2E tests for core flows (enrollment, certificate download)
- [ ] Role-based UI improvements and access controls

---

## 📬 Contact & Support

For questions, feature requests, or help setting up, open an issue or contact the repo owner.

---

Thank you for building EduMaster — want me to:
- add a ready-to-use `.env.example` for both backend and frontend,
- create Dockerfiles + docker-compose and push them here, or
- extract exact build commands from the repo (`pom.xml` and `package.json`) and update the Quick Start with exact commands?

I can proceed with any of the above — tell me which one and I'll generate the files.
