import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getTeacherDashboard } from "../api/teacherApi";
import { getMyCourses } from "../api/teacherCourseApi";
import "./TeacherDashboard.css";

const TeacherDashboard = () => {
  const navigate = useNavigate();

  const [stats, setStats] = useState(null);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const [dashboardRes, coursesRes] = await Promise.all([
          getTeacherDashboard(),
          getMyCourses(),
        ]);

        setStats(dashboardRes.data);
        setCourses(coursesRes.data || []);
      } catch (err) {
        console.error("Teacher dashboard load failed", err);
        setStats(null);
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  if (loading) return <p className="page-loading">Loading dashboard...</p>;
  if (!stats) return <p className="error-text">Failed to load dashboard</p>;

  return (
    <div className="teacher-dashboard">
      <div className="dashboard-header">
        <h1>Teacher Dashboard</h1>

        <button
          className="btn-primary"
          onClick={() => navigate("/teacher/courses/new")}
        >
          + Create Course
        </button>
      </div>

      {/* ===== STATS ===== */}
      <div className="stats-grid">
        <StatCard label="Courses" value={stats.totalCourses} />
        <StatCard label="Students" value={stats.totalStudents} />
        <StatCard label="Earnings" value={`$${stats.totalEarnings}`} />
      </div>

      {/* ===== COURSES ===== */}
      <div className="dashboard-section">
        <div className="section-header">
          <h2>Your Courses</h2>

          <button
            className="btn-outline"
            onClick={() => navigate("/teacher/courses")}
          >
            Manage Courses
          </button>
        </div>

        {courses.length === 0 ? (
          <p className="muted">You haven’t created any courses yet.</p>
        ) : (
          <div className="dashboard-course-list">
            {courses.map((course) => (
              <div key={course.courseId} className="dashboard-course-card">
                <div className="course-info">
                  <h3>{course.title}</h3>
                  <p className="muted">{course.category || "Uncategorized"}</p>
                </div>

                <div className="course-actions">
                  <button
                    className="btn-secondary"
                    onClick={() =>
                      navigate(`/teacher/courses/${course.courseId}/lectures`)
                    }
                  >
                    Lectures
                  </button>

                  <button
                    className="btn-outline"
                    onClick={() =>
                      navigate(`/teacher/courses?edit=${course.courseId}`)
                    }
                  >
                    Edit
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

const StatCard = ({ label, value }) => (
  <div className="stat-card">
    <p>{label}</p>
    <h3>{value}</h3>
  </div>
);

export default TeacherDashboard;
