import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout.jsx";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import BrowseCourses from "./pages/BrowseCourses";
import Profile from "./pages/Profile";
import Settings from "./pages/Settings";
import CourseDetails from "./pages/CourseDetails.jsx";
import LecturePlayer from "./pages/LecturePlayer";
import ProtectedRoute from "./auth/ProtectedRoute";
import TeacherDashboard from "./pages/TeacherDashboard";
import TeacherProfile from "./pages/TeacherProfile";
import TeacherCourses from "./pages/teacher/TeacherCourses.jsx";
import CreateCourse from "./pages/CreateCourse.jsx";
import TeacherLectures from "./pages/teacher/TeacherLectures.jsx";
import ManageQuizPage from "./pages/teacher/ManageQuizPage.jsx";
import QuizResultsPage from "./pages/teacher/QuizResultsPage.jsx";
import TeacherRevenuePage from "./pages/teacher/TeacherRevenuePage.jsx";
import QuizPage from "./pages/student/QuizPage.jsx";
import EnrollCourse from "./pages/EnrollCourse.jsx";
import PaymentPage from "./pages/PaymentPage";
import AdminProfile from "./pages/admin/AdminProfile.jsx";
import AdminLayout from "./pages/admin/AdminLayout.jsx";
import AdminDashboard from "./pages/admin/AdminDashboard.jsx";
import AdminTeachers from "./pages/admin/teachers.jsx";
import AdminCourses from "./pages/admin/courses.jsx";
import AdminStudents from "./pages/admin/students.jsx";
import AdminReports from "./pages/admin/reports.jsx";
import AdminRevenuePage from "./pages/admin/AdminRevenuePage.jsx";
import AdminTeacherView from "./pages/admin/AdminTeacherView.jsx";
import AdminTeacherEdit from "./pages/admin/AdminTeacherEdit.jsx";
import AdminPayments from "./pages/admin/AdminPayments.jsx";
import Mentors from "./pages/Mentors";
import Path from "./pages/Path";
import "./App.css";

function App() {
  return (
    <div className="app-layout">
      <Routes>
        {/* ================= PUBLIC ROUTES ================= */}
        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/browse" element={<BrowseCourses />} />
          <Route path="/paths" element={<Path />} />
          <Route path="/mentors" element={<Mentors />} />
        </Route>

        {/* ================= AUTH ROUTES ================= */}
        <Route element={<Layout />}>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/reset-password" element={<ResetPassword />} />
        </Route>

        {/* ================= SETTINGS (ALL ROLES) ================= */}
        <Route
          element={
            <ProtectedRoute allowedRoles={["STUDENT", "TEACHER", "ADMIN"]} />
          }
        >
          <Route element={<Layout />}>
            <Route path="/settings" element={<Settings />} />
          </Route>
        </Route>

        {/* ================= STUDENT ROUTES ================= */}
        <Route element={<ProtectedRoute allowedRoles={["STUDENT"]} />}>
          <Route element={<Layout />}>
            <Route path="/profile" element={<Profile />} />
            <Route path="/course/:courseId" element={<CourseDetails />} />
            <Route path="/course/:courseId/enroll" element={<EnrollCourse />} />
            <Route path="/course/:courseId/payment" element={<PaymentPage />} />
            <Route
              path="/course/:courseId/lecture/:lectureId"
              element={<LecturePlayer />}
            />
            <Route path="/course/:courseId/quiz" element={<QuizPage />} />
          </Route>
        </Route>

        {/* ================= TEACHER ROUTES ================= */}
        <Route element={<ProtectedRoute allowedRoles={["TEACHER"]} />}>
          <Route element={<Layout />}>
            <Route path="/teacher/dashboard" element={<TeacherDashboard />} />
            <Route path="/teacher/profile" element={<TeacherProfile />} />
            <Route path="/teacher/:teacherId" element={<TeacherProfile />} />
            <Route path="/teacher/courses" element={<TeacherCourses />} />
            <Route path="/teacher/courses/new" element={<CreateCourse />} />
            <Route
              path="/teacher/courses/:courseId/lectures"
              element={<TeacherLectures />}
            />
            <Route path="/teacher/revenue" element={<TeacherRevenuePage />} />
            <Route
              path="/teacher/courses/:courseId/quiz"
              element={<ManageQuizPage />}
            />
            <Route
              path="/teacher/courses/:courseId/quiz-results"
              element={<QuizResultsPage />}
            />
          </Route>
        </Route>
        {/* ================== ADMIN ROUTES ================= */}
        <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route index element={<AdminDashboard />} />
            <Route path="dashboard" element={<AdminDashboard />} />
            <Route path="teachers" element={<AdminTeachers />} />
            <Route path="teachers/:id" element={<AdminTeacherView />} />
            <Route path="teachers/:id/edit" element={<AdminTeacherEdit />} />
            <Route path="students" element={<AdminStudents />} />
            <Route path="courses" element={<AdminCourses />} />
            <Route path="reports" element={<AdminReports />} />
            <Route path="payments" element={<AdminPayments />} />
            <Route path="revenue" element={<AdminRevenuePage />} />
          </Route>
          <Route path="/admin/profile" element={<AdminProfile />} />
        </Route>
      </Routes>
    </div>
  );
}

export default App;
