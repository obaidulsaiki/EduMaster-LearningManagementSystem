<div align="center">

# 🎓 EduMaster - Learning Management System

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/React-19.2.0-61DAFB?style=for-the-badge&logo=react&logoColor=black" alt="React">
  <img src="https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT">
  <img src="https://img.shields.io/badge/AI-Powered-FF6F00?style=for-the-badge&logo=openai&logoColor=white" alt="AI">
</p>

**A comprehensive, production-ready Learning Management System secured with `JWT` and powered by `PostgreSQL`**

[Features](#-features) • [Tech Stack](#-tech-stack) • [API Documentation](#-rest-api-endpoints) • [Installation](#-installation) • [Screenshots](#-screenshots)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [REST API Endpoints](#-rest-api-endpoints)
- [Dependencies](#-dependencies)
- [Installation & Setup](#-installation--setup)
- [Database Schema](#-database-schema)
- [Security](#-security)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🌟 Overview

**EduMaster** is a modern, full-stack Learning Management System designed for educational institutions, online course creators, and learners. Built with Spring Boot and React, it provides a seamless experience for managing courses, tracking progress, and facilitating online learning with AI-powered assistance.

### 👥 User Roles

- **🎓 Students** - Browse, enroll, learn, and track progress
- **👨‍🏫 Teachers** - Create courses, manage content, track revenue
- **👨‍💼 Admins** - Oversee platform, manage users, generate reports

---

## ✨ Features

### 🎯 Core Features

#### For Students
- 📚 **Course Browsing & Enrollment** - Advanced filtering by category, price, rating
- 🎥 **Video Lecture Player** - Seamless learning experience with progress tracking
- 📝 **Quiz System** - Interactive assessments with instant feedback
- 📊 **Progress Tracking** - Monitor course completion and performance
- 🏆 **Certificate Generation** - Earn PDF certificates upon course completion
- ⭐ **Course Reviews** - Rate and review completed courses
- 💳 **Secure Payments** - Integrated payment processing for enrollments
- 📜 **Invoice Management** - Download payment receipts and invoices
- 🤖 **AI Career Assistant** - Personalized learning paths based on bio, history, and 21+ industry roadmaps
- ❤️ **Course Wishlist** - Save favorite courses to study later
- 🗺️ **CSE Job Paths** - Comprehensive "Beginning-to-End" roadmaps for 21+ tech roles
- 📤 **Assignment Uploads** - Submit course-related assignments for evaluation

#### For Teachers
- ➕ **Course Creation** - Rich course builder with multimedia support
- 🎬 **Lecture Management** - Upload, organize, and reorder video lectures
- 📋 **Quiz Builder** - Create assessments with multiple question types
- 💰 **Revenue Tracking** - Real-time earnings and analytics dashboard
- 💸 **Withdrawal System** - Request payouts and track withdrawal history
- 📈 **Analytics Dashboard** - Student enrollment and engagement metrics
- 👨‍🎓 **Student Management** - View enrolled students and their progress
- ✏️ **Profile Management** - Showcase credentials, education, and experience

#### For Administrators
- 🎛️ **Admin Dashboard** - Comprehensive platform analytics
- 👥 **User Management** - Manage students, teachers, and permissions
- 📚 **Course Moderation** - Approve, publish, or remove courses
- 💵 **Payment Processing** - Handle withdrawals and monitor transactions
- 📊 **Report Generation** - Generate detailed analytics and reports
- 🔍 **Search & Filter** - Advanced search across all entities
- 🚫 **Ban/Suspension** - Manage user access and platform rules
- 📧 **System Communications** - Automated email notifications for account activities
- 📜 **Audit Logs** - Track and monitor administrative actions for transparency

### 🔐 Authentication & Security
- 🛡️ **JWT-based Authentication** - Secure role-based access control (RBAC)
- 🔑 **Forgot Password Flow** - Robust email-based password recovery mechanism
- ✅ **Secure Updates** - OTP verification required for sensitive actions (email/password changes)
- 📧 **Verification System** - Integrated one-time password (OTP) service via email
- 🔒 **Secure Hashing** - Industry-standard BCrypt password encryption
- 🚪 **Protected Routes** - Strict access control for student, teacher, and admin areas

### 📱 Modern UI/UX (Premium Features)
- ✨ **Glassmorphism Design** - High-end, translucent interface components
- 🌊 **Dynamic Navbar** - Scroll-aware transitions from transparent to blurred glass
- 🌙 **Advanced Dark Mode** - Fully consistent dark theme across all components
- 🚀 **Smooth Transitions** - Professional animations and interactive elements
- 📱 **Mobile First** - Perfectly responsive on all device sizes

### 🤖 AI-Powered Features
- 🧠 **Context-Aware Learning** - Intelligent course-related Q&A with context memory
- 🔍 **Natural Language Search** - Find courses using conversational queries
- 📝 **Intelligent Quiz Generation** - AI-assisted assessment creation for teachers
- 🎯 **Personalized Recommendations** - Smart course suggestions based on user behavior
- ⚡ **Local AI Processing** - Powered by Ollama for privacy-focused local execution

---

## 🛠 Tech Stack

The EduMaster LMS is built using a modern, high-performance stack designed for scalability, security, and a premium "wow" user experience.

### 🍃 Backend (Spring Ecosystem)

Built on **Spring Boot 4.0.0** and **Java 17 (LTS)**, the backend leverages the full power of the Spring Ecosystem, configured for a stateless, secure, and real-time learning environment.

| Technology | Detail | Purpose |
| :--- | :--- | :--- |
| **Spring Boot** | Version 4.0.0 | Core application shell and auto-configuration engine. |
| **Spring Security** | JWT-Based | Robust authentication with role-based access control (RBAC). |
| **Spring Data JPA**| Hibernate-based | Efficient ORM and data persistence via PostgreSQL. |
| **Spring WebMVC** | RESTful | High-performance API architecture for frontend communication. |
| **Spring WebSocket**| STOMP Protocol | Real-time bi-directional messaging for instant notifications. |
| **Spring Mail** | JavaMail | Automated delivery of OTPs, certificates, and invoices. |
| **LangChain4j** | AI Integration | Intelligent RAG (Retrieval-Augmented Generation) and LLM memory (v0.33.0). |
| **Ollama** | Local AI | Privacy-focused local execution of models like Llama 3.1. |
| **PostgreSQL** | Relational DB | Enterprise-grade SQL database for reliable data storage. |
| **OpenPDF** | PDF Engine | Generates high-quality PDF certificates and invoices (v1.3.30). |
| **Jackson** | Data Format | High-speed JSON and XML serialization/deserialization. |
| **Lombok** | Productivity | Annotation-based reduction of boilerplate code. |
| **Spring DevTools**| Developer Tool | Enhanced productivity with hot swapping and restarts. |
| **Maven** | Build Tool | Comprehensive project lifecycle and dependency management. |

### ⚛️ Frontend (React Ecosystem)

A cutting-edge Single Page Application (SPA) built with **React 19** and a luxurious, mobile-first design system. The frontend prioritizes visual excellence with glassmorphism effects and fluid motion.

| Library | Version | Purpose |
| :--- | :---: | :--- |
| **React** | 19.2.0 | Core UI architecture utilizing the latest concurrent features. |
| **Vite** | 7.2.4 | Lightning-fast build pipeline and ultra-responsive HMR. |
| **React Router** | 7.11.0 | Sophisticated client-side routing and protected navigation. |
| **Framer Motion** | 12.29.2 | Premium physics-based animations and professional transitions. |
| **Recharts** | 3.7.0 | Interactive SVG data visualization for student and teacher analytics. |
| **Lucide React** | 0.562.0 | Modern, lightweight, and customizable SVG icon system. |
| **Axios** | 1.13.2 | Reliable API communication with request/response interceptors. |
| **SockJS / Stomp** | 1.6 / 2.3 | Client-side WebSocket integration for live system updates. |
| **JWT Decode** | 4.0.0 | Client-side authorization token parsing and verification. |
| **ESLint** | 9.39.1 | Integrated linting for maintaining high code quality. |

---

## 📁 Project Structure

### Backend Structure

```
lms/
├── src/main/java/com/example/lms/
│   ├── config/                    # 8 configuration files
│   │   ├── AdminSeeder.java       # Default admin setup
│   │   ├── JacksonConfig.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtService.java
│   │   ├── RoadmapData.java       # AI Industry Roadmaps Context
│   │   ├── SecurityConfig.java    # Spring Security setup
│   │   ├── WebConfig.java         # CORS & Web config
│   │   └── WebSocketConfig.java
│   │
│   ├── controller/                # 30 REST controllers
│   │   ├── AdminController.java
│   │   ├── ...
│   │   ├── AssignmentController.java # New: Assignment management
│   │   └── WishlistController.java
│   │
│   ├── service/                   # 34 service classes
│   │   ├── AdminCourseService.java
│   │   ├── ...
│   │   ├── AuditLogService.java      # New: Audit logging
│   │   └── WithdrawalService.java
│   │
│   ├── entity/                    # 32 database entities
│   │   ├── Admin.java
│   │   ├── ...
│   │   ├── AssignmentSubmission.java # New: Student submissions
│   │   ├── AuditLog.java             # New: Admin activity log
│   │   └── WithdrawalRequest.java
│   │
│   ├── dto/                       # 61 data transfer objects
│   ├── repository/                # 27 JPA repositories
│   └── LmsApplication.java
│   └── LmsApplication.java
│
├── src/main/resources/
│   └── application.properties     # Database & JWT config
├── pom.xml                        # Maven dependencies
└── target/                        # Build output
```

### Frontend Structure

```
lms-frontend/
├── src/
│   ├── api/                       # 22 API service files
│   │   ├── adminApi.js
│   │   ├── authApi.js             # New: Auth APIs
│   │   ├── ...
│   │   └── wishlistApi.js
│   │
│   ├── pages/                     # 40 page components
│   │   ├── admin/                 # Admin specific pages
│   │   ├── student/               # Student specific pages
│   │   ├── teacher/               # Teacher specific pages
│   │   ├── ...
│   │   └── TeacherProfile.jsx
│   │
│   ├── components/                # 20 reusable components
│   │   ├── Admin/
│   │   ├── profile/
│   │   ├── settings/
│   │   ├── teacher/
│   │   └── ...
│   │
│   ├── auth/                      # 2 authentication files
│   ├── context/                   # 1 React context
│   ├── services/                  # 3 business logic services
│   ├── utils/                     # 1 utility file
│   ├── App.jsx                    # Main app component
│   ├── App.css
│   ├── main.jsx                   # Entry point
│   ├── index.css                  # Global styles
│   └── theme.js                   # Theme configuration
│
├── public/
├── package.json                   # NPM dependencies
├── vite.config.js                 # Vite configuration
├── eslint.config.js               # ESLint rules
└── index.html                     # HTML template
```

---

## 🔌 REST API Endpoints

The backend exposes an exact total of **104 RESTful API endpoints** organized by domain:

### 📊 API Statistics

| Category | Endpoints | Description |
|----------|-----------|-------------|
| **Authentication** | 5 | Login, register, email verification, password recovery |
| **Courses (Public)** | 3 | Browse, filter, view course details |
| **Student APIs** | 17 | Profile, enrollment, progress, certificates, invoices, assignments |
| **Teacher APIs** | 28 | Course management, lectures, revenue, quiz builder |
| **Admin APIs** | 30 | Dashboard, user management, reports, payments, audit logs |
| **AI Assistance** | 1 | Chat, career paths, recommendations |
| **Wishlist** | 3 | Add, remove, view favorites |
| **Reviews & Ratings** | 2 | Submit and view reviews |
| **Quizzes** | 6 | Student & Teacher quiz interactions |
| **Education** | 4 | CRUD for education credentials |
| **Settings** | 5 | Email, password, preferences, account deletion |
| **Total** | **104** | **Meticulously documented REST APIs** |

### 🔐 Authentication Endpoints

```
POST   /api/auth/register            # Register new user
POST   /api/auth/login               # Login user
GET    /api/auth/check-email         # Check email availability
POST   /api/auth/forgot-password     # Request password reset code
POST   /api/auth/reset-password      # Reset password with OTP
```

### 📚 Course Management Endpoints

#### Public Course APIs
```
GET    /api/courses/filter         # Browse courses with filters
GET    /api/courses/{id}           # Get course details
GET    /api/courses/categories     # Get all categories
```

#### Teacher Course APIs
```
POST   /api/teacher/courses                  # Create course
GET    /api/teacher/courses/my               # Get my courses
PUT    /api/teacher/courses/{id}             # Update course
DELETE /api/teacher/courses/{id}             # Delete course
PUT    /api/teacher/courses/{id}/publish     # Publish course
PUT    /api/teacher/courses/{id}/unpublish   # Unpublish course
```

#### Admin Course APIs
```
GET    /api/admin/courses                    # List all courses
GET    /api/admin/courses/{id}               # Get course details
PUT    /api/admin/courses/{id}/toggle        # Toggle course status
DELETE /api/admin/courses/{id}               # Delete course
PUT    /api/admin/courses/{id}/publish       # Toggle publish status
```

### 🎥 Lecture Management Endpoints

```
GET    /api/lectures/{courseId}                          # Get course lectures
POST   /api/teacher/courses/{courseId}/lectures          # Create lecture
PUT    /api/teacher/lectures/{id}                        # Update lecture
DELETE /api/teacher/lectures/{id}                        # Delete lecture
PUT    /api/teacher/courses/{courseId}/lectures/reorder  # Reorder lectures
```

### 🎓 Student Endpoints

#### Enrollment
```
POST   /api/student/enroll/{courseId}         # Enroll in course
POST   /api/student/enroll/{courseId}/confirm # Confirm enrollment
GET    /api/student/enroll/{courseId}/status  # Check enrollment status
```

#### Progress Tracking
```
GET    /api/progress/courses                  # Get enrolled courses
GET    /api/progress/courses/{id}             # Get course progress
POST   /api/progress/lectures/{id}/complete   # Mark lecture complete
POST   /api/progress/lectures/{id}/incomplete # Mark lecture incomplete
GET    /api/progress/courses/{id}/quizzes     # Get course quizzes
```

#### Quiz System
```
GET    /api/quiz/course/{courseId}            # Get course quiz
POST   /api/quiz/submit                       # Submit quiz answers
GET    /api/quiz/result/{courseId}            # Get quiz results
```

#### Certificates
```
GET    /api/certificates/available            # Get available certificates
GET    /api/certificates/download/{courseId}  # Download certificate PDF
GET    /api/certificates/{courseId}/exists    # Check certificate exists
```

### 👨‍🏫 Teacher Endpoints

#### Dashboard & Profile
```
GET    /api/teacher/dashboard                 # Teacher dashboard data
GET    /api/teacher/profile/me                # Get my profile
POST   /api/teacher/profile                   # Create/update profile
```

#### Education & Experience
```
GET    /api/teacher/education/me              # Get my education
POST   /api/teacher/education                 # Add education
PUT    /api/teacher/education/{id}            # Update education
DELETE /api/teacher/education/{id}            # Delete education
GET    /api/teacher/experience/me             # Get my experience
POST   /api/teacher/experience                # Add experience
PUT    /api/teacher/experience/{id}           # Update experience
DELETE /api/teacher/experience/{id}           # Delete experience
```

#### Quiz Management
```
POST   /api/quiz/manage/{courseId}            # Create/update quiz
GET    /api/quiz/manage/{courseId}            # Get quiz for editing
GET    /api/quiz/results/{courseId}           # Get student results
```

#### Revenue & Withdrawals
```
GET    /api/revenue/teacher/summary           # Revenue summary
POST   /api/revenue/withdraw                  # Request withdrawal
GET    /api/revenue/my-withdrawals            # Withdrawal history
```

### 👨‍💼 Admin Endpoints

#### Dashboard
```
GET    /api/admin/dashboard                   # Admin dashboard stats
```

#### User Management
```
GET    /api/admin/students                    # List all students
GET    /api/admin/students/{id}               # Get student details
DELETE /api/admin/student/{id}                # Ban student
GET    /api/admin/teachers                    # List all teachers
GET    /api/admin/teachers/{id}               # Get teacher details  
DELETE /api/admin/teacher/{id}                # Ban teacher
PUT    /api/admin/teachers/{id}/toggle        # Toggle teacher status
```

#### Payment Management
```
GET    /api/admin/payments                    # List all payments
GET    /api/admin/revenue/summary             # Revenue summary
GET    /api/admin/all-pending                 # Pending withdrawals
POST   /api/admin/complete/{id}               # Complete withdrawal
GET    /api/admin/all-history                 # Payment history
POST   /api/revenue/sync                      # Sync revenue data
```

#### Reports
```
GET    /api/admin/reports/enrollments         # Enrollment reports
GET    /api/admin/reports/revenue             # Revenue reports
GET    /api/admin/reports/course-performance  # Course analytics
```

### 🤖 AI Assistant Endpoints

```
POST   /api/ai/chat                           # Chat with AI assistant
```

### ⭐ Review Endpoints

```
POST   /api/reviews                           # Submit review
GET    /api/reviews/course/{courseId}         # Get course reviews
```

### 📬 Notification Endpoints

```
GET    /api/notifications                     # Get notifications
PUT    /api/notifications/{id}/read           # Mark as read
```

### ⚙️ Settings Endpoints

```
PUT    /api/settings/email              # Change email (requires OTP)
PUT    /api/settings/password           # Change password (requires OTP)
POST   /api/settings/request-verification # Request OTP for email/password change
PUT    /api/settings/preferences        # Update preferences
DELETE /api/settings                    # Delete account
```

### 📄 Invoice Endpoints

```
GET    /api/invoices                          # Get user invoices
GET    /api/invoices/{id}/download            # Download invoice PDF
```

### 🏫 Public Teacher Endpoints

```
GET    /api/public/teachers                   # Browse teachers/mentors
```

---

## 📦 Dependencies

### Backend Dependencies (Maven)

<details>
<summary><b>Click to expand full dependency list</b></summary>

```xml
<!-- Core Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT Authentication -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- PDF Generation -->
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.30</version>
</dependency>

<!-- Database -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Jackson JSON/XML -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>

<!-- LangChain4j AI -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-spring-boot-starter</artifactId>
    <version>0.33.0</version>
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-ollama-spring-boot-starter</artifactId>
    <version>0.33.0</version>
</dependency>

<!-- DevTools -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

</details>

### Frontend Dependencies (NPM)

<details>
<summary><b>Click to expand full dependency list</b></summary>

**Production Dependencies:**
```json
{
  "axios": "^1.13.2",           // HTTP client for API calls
  "jwt-decode": "^4.0.0",       // JWT token decoding
  "lucide-react": "^0.562.0",   // Modern icon library
  "react": "^19.2.0",           // UI framework
  "react-dom": "^19.2.0",       // React DOM renderer
  "react-router-dom": "^7.11.0" // Client-side routing
}
```

**Development Dependencies:**
```json
{
  "@eslint/js": "^9.39.1",
  "@types/react": "^19.2.5",
  "@types/react-dom": "^19.2.3",
  "@vitejs/plugin-react": "^5.1.1",
  "eslint": "^9.39.1",
  "eslint-plugin-react-hooks": "^7.0.1",
  "eslint-plugin-react-refresh": "^0.4.24",
  "globals": "^16.5.0",
  "vite": "^7.2.4"               // Build tool & dev server
}
```

</details>

---

## 🚀 Installation & Setup

### Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **PostgreSQL 14** or higher
- **Maven 3.8** or higher
- **Ollama** (for AI features) - Optional

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/obaidulsaiki/EduMaster-LearningManagementSystem.git
   cd EduMaster-LearningManagementSystem/lms
   ```

2. **Configure PostgreSQL Database**
   
   Create a new database:
   ```sql
   CREATE DATABASE lms;
   ```

3. **Update `application.properties`**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/lms
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # JWT Secret (Generate your own secure key)
   jwt.secret-key=your-secret-key-here
   ```

4. **Install dependencies and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Backend will run on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd lms-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure environment variables**
   
   Create `.env` file:
   ```env
   VITE_API_URL=http://localhost:8080
   ```

4. **Run development server**
   ```bash
   npm run dev
   ```

   Frontend will run on `http://localhost:5173`

### AI Setup (Optional)

1. **Install Ollama**
   ```bash
   # Visit https://ollama.ai for installation instructions
   ```

2. **Pull AI model**
   ```bash
   ollama pull gemma3:4b
   ```

3. **Start Ollama service**
   ```bash
   ollama serve
   ```

### Default Admin Credentials

After the first run, a default admin account is created:
```
Email: admin@edumaster.com
Password: admin123
```

> **⚠️ Important:** Change the default admin password immediately after first login!

---

## 🗄️ Database Schema

### Core Entities

#### User Management
- `base_user` - Base user information (polymorphic)
- `student` - Student-specific data
- `teacher` - Teacher-specific data
- `admin` - Administrator data
- `student_profile` - Student profile details
- `teacher_profile` - Teacher profile details
- `admin_profile` - Admin profile details

#### Education System
- `course` - Course information
- `lecture` - Video lectures
- `quiz` - Course quizzes
- `question` - Quiz questions
- `enrollment` - Student course enrollments
- `course_progress` - Student progress tracking
- `completed_lecture` - Lecture completion records
- `quiz_result` - Quiz attempt results

#### Financial
- `payment` - Payment transactions
- `withdrawal_request` - Teacher withdrawal requests

#### Additional Features
- `review` - Course reviews and ratings
- `certificate` - Generated certificates
- `notification` - User notifications
- `education` - Education credentials
- `teacher_education` - Teacher education records
- `teacher_experience` - Teacher work experience
- `ai_conversation` - AI chat history
- `wishlist` - Student favorite courses

### Entity Relationships

```
Student 1──* Enrollment *──1 Course 1──* Teacher
Enrollment 1──1 CourseProgress 1──* CompletedLecture *──1 Lecture
Course 1──* Lecture
Course 1──1 Quiz 1──* Question
Student 1──* QuizResult *──1 Quiz
Student 1──* Review *──1 Course
Student 1──* Certificate *──1 Course
Teacher 1──* WithdrawalRequest
Student 1──* Payment *──1 Course
```

---

## 🔐 Security

### Authentication Flow

1. User registers/logs in via `/api/auth/login` or `/api/auth/register`
2. Backend validates credentials and generates JWT token
3. Token contains user ID, email, and role (STUDENT, TEACHER, ADMIN)
4. Client stores token in localStorage
5. All subsequent requests include token in Authorization header
6. Backend validates token via `JwtAuthenticationFilter`
7. Access granted based on role and endpoint permissions

### Security Features

- **Password Hashing**: BCrypt with configurable strength
- **JWT Tokens**: Signed with HS256 algorithm
- **CORS Protection**: Configured allowed origins
- **Role-Based Access**: Endpoint protection by user role
- **SQL Injection Prevention**: JPA parameterized queries
- **XSS Protection**: Input validation and sanitization
- **CSRF Protection**: Token-based verification

### Protected Routes

| Role | Access Level |
|------|-------------|
| **PUBLIC** | Login, register, browse courses, view course details |
| **STUDENT** | Enroll, watch lectures, take quizzes, view progress, certificates |
| **TEACHER** | Create courses, manage lectures, view analytics, request withdrawals |
| **ADMIN** | Full platform access, user management, reports, payment processing |

---

## 📸 Screenshots

> Add your application screenshots here

---

## 🎯 API Best Practices

- **Versioning**: All endpoints prefixed with `/api`
- **RESTful Design**: Proper HTTP verbs (GET, POST, PUT, DELETE)
- **Status Codes**: Appropriate HTTP status codes
- **Error Handling**: Consistent error response format
- **Pagination**: Page-based pagination for list endpoints
- **Filtering**: Query parameters for filtering and searching
- **Sorting**: Configurable sort options
- **DTO Pattern**: Clean separation of entities and API responses

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Obaidul Saiki**

- GitHub: [@obaidulsaiki](https://github.com/obaidulsaiki)
- Repository: [EduMaster-LearningManagementSystem](https://github.com/obaidulsaiki/EduMaster-LearningManagementSystem)

---

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- React Team for the powerful UI library
- LangChain4j for AI integration capabilities
- PostgreSQL Community for the robust database
- All open-source contributors

---

<div align="center">

### ⭐ Star this repository if you find it helpful!

**Made with ❤️ by Obaidul Saiki**

</div>
