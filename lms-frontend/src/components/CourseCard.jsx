import React from "react";
import { useNavigate } from "react-router-dom";
import { BookOpen, ArrowRight } from "lucide-react";
import "./CourseCard.css";

const CourseCard = ({ course }) => {
  const navigate = useNavigate();

  const handleViewCourse = () => {
    navigate(`/course/${course.courseId}`);
  };

  const titleInitials = course.title
    ?.split(" ")
    .slice(0, 2)
    .map((w) => w[0])
    .join("")
    .toUpperCase();

  return (
    <div className="course-card" onClick={handleViewCourse}>
      {/* ================= THUMBNAIL ================= */}
      <div className="course-thumb">
        <span className="course-initials">{titleInitials}</span>
        <span className="category-badge">{course.category || "General"}</span>
      </div>

      {/* ================= CONTENT ================= */}
      <div className="course-info">
        <div className="course-header">
          <h3 className="course-title">{course.title}</h3>

          <span className={`price-tag ${course.price === 0 ? "free" : ""}`}>
            {course.price === 0 ? "Free" : `$${course.price}`}
          </span>
        </div>

        <p className="course-description">
          {course.description
            ? course.description.length > 90
              ? course.description.substring(0, 90) + "..."
              : course.description
            : "No description provided"}
        </p>

        {/* ================= FOOTER ================= */}
        <div className="course-footer">
          <div className="meta-item">
            <BookOpen size={14} />
            <span>{course.lectureCount ?? 0} Lectures</span>
          </div>

          <button className="view-btn">
            View <ArrowRight size={14} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default CourseCard;
