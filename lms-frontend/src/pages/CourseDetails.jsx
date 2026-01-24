import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getCourseDetails } from "../api/courseApi";
import { getEnrollmentStatus } from "../api/studentEnrollmentApi";
import "./CourseDetails.css";

const CourseDetails = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();

  const [course, setCourse] = useState(null);
  const [enrolled, setEnrolled] = useState(false);
  const [progress, setProgress] = useState(0);
  const [resumeLectureId, setResumeLectureId] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        /* ===== COURSE ===== */
        const courseRes = await getCourseDetails(courseId);

        const lectures = [...(courseRes.data.lectures || [])].sort(
          (a, b) => (a.orderIndex ?? 0) - (b.orderIndex ?? 0),
        );

        setCourse({ ...courseRes.data, lectures });

        /* ===== ENROLLMENT ===== */
        try {
          const statusRes = await getEnrollmentStatus(courseId);

          if (statusRes.data.enrolled) {
            setEnrolled(true);
            setProgress(statusRes.data.progress || 0);
            setResumeLectureId(statusRes.data.resumeLectureId || null);
          } else {
            setEnrolled(false);
          }
        } catch {
          setEnrolled(false);
        }
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [courseId]);

  if (loading) return <p>Loading...</p>;
  if (!course) return <p>Course not found</p>;

  const lectures = course.lectures || [];

  /* ================= CURRENT & NEXT ================= */
  const currentIndex = resumeLectureId
    ? lectures.findIndex((l) => String(l.id) === String(resumeLectureId))
    : -1;

  const nextLecture =
    currentIndex >= 0
      ? lectures[currentIndex + 1] || null
      : lectures[0] || null;

  /* ================= CTA ================= */
  const handlePrimaryAction = () => {
    if (!enrolled) {
      navigate(`/course/${courseId}/enroll`);
      return;
    }

    if (!nextLecture) return;

    navigate(`/course/${courseId}/lecture/${nextLecture.id}`);
  };

  return (
    <div className="course-details">
      <h2>{course.title}</h2>
      <p>{course.description}</p>

      {/* ===== PROGRESS ===== */}
      {enrolled && (
        <div className="course-progress">
          <span>Progress: {progress}%</span>
          <div className="progress-bar">
            <div className="progress-fill" style={{ width: `${progress}%` }} />
          </div>
        </div>
      )}

      {/* ===== CTA ===== */}
      <button className="start-btn" onClick={handlePrimaryAction}>
        {!enrolled
          ? "Enroll Now"
          : !resumeLectureId
            ? "▶ Start Course"
            : progress === 100
              ? "✔ Course Completed"
              : "▶ Continue Course"}
      </button>

      {/* ===== LECTURES ===== */}
      <h3>Lectures</h3>

      <ul className="lecture-list">
        {lectures.map((lecture, index) => {
          const locked =
            !enrolled || (currentIndex !== -1 && index > currentIndex + 1);

          return (
            <li
              key={lecture.id}
              className={`lecture-item ${locked ? "locked" : ""}`}
              onClick={() =>
                !locked && navigate(`/course/${courseId}/lecture/${lecture.id}`)
              }
            >
              {lecture.title}

              {lecture.id === resumeLectureId && (
                <span className="resume-badge">Current</span>
              )}

              {locked && <span> 🔒</span>}
            </li>
          );
        })}
      </ul>
    </div>
  );
};

export default CourseDetails;
