import React from "react";
import "./CourseCard.css";

const CourseCard = ({ course, onManage }) => {
  return (
    <div className="course-card">
      <h3>{course.title}</h3>
      <p className="description">{course.description}</p>

      <div className="course-meta">
        <span className={`status ${course.published ? "published" : "draft"}`}>
          {course.published ? "Published" : "Draft"}
        </span>
      </div>

      <button className="secondary-btn" onClick={onManage}>
        Manage Course
      </button>
    </div>
  );
};

export default CourseCard;
