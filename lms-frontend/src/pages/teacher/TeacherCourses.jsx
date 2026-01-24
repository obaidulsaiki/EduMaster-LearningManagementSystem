import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getMyCourses,
  publishCourse,
  unpublishCourse,
} from "../../api/teacherCourseApi";

import "./TeacherCourses.css";

const TeacherCourses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadCourses = async () => {
    try {
      const res = await getMyCourses();
      setCourses(res.data || []);
    } catch {
      alert("Failed to load courses");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCourses();
  }, []);

  if (loading) return <p className="page-loading">Loading...</p>;

  return (
    <div className="teacher-courses-page">
      <div className="page-header">
        <h2>My Courses</h2>

        <button
          className="primary-btn"
          onClick={() => navigate("/teacher/courses/new")}
        >
          + Create Course
        </button>
      </div>

      {courses.length === 0 ? (
        <p className="muted">You haven't created any courses yet.</p>
      ) : (
        <div className="course-grid">
          {courses.map((course) => (
            <div key={course.courseId} className="teacher-course-card">
              <div className="course-info">
                <h3>{course.title}</h3>
                <p className="muted">{course.category || "Uncategorized"}</p>

                <span
                  className={`status ${
                    course.published ? "published" : "draft"
                  }`}
                >
                  {course.published ? "Published" : "Draft"}
                </span>
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

                {course.published ? (
                  <button
                    className="btn-outline"
                    onClick={async () => {
                      await unpublishCourse(course.courseId);
                      loadCourses();
                    }}
                  >
                    Unpublish
                  </button>
                ) : (
                  <button
                    className="btn-primary"
                    onClick={async () => {
                      await publishCourse(course.courseId);
                      loadCourses();
                    }}
                  >
                    Publish
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default TeacherCourses;
