import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getCourseDetails } from "../api/courseApi";
import { getEnrollmentStatus, enrollCourse } from "../api/studentEnrollmentApi";
import { quizApi } from "../api/quizApi";
import { downloadCertificate } from "../api/certificateApi";
import reviewApi from "../api/reviewApi";
import { toggleWishlist, checkWishlistStatus } from "../api/wishlistApi";
import { Heart } from "lucide-react";
import "./CourseDetails.css";

const CourseDetails = () => {
    const { courseId } = useParams();
    const navigate = useNavigate();

    const [course, setCourse] = useState(null);
    const [enrolled, setEnrolled] = useState(false);
    const [progress, setProgress] = useState(0);
    const [resumeLectureId, setResumeLectureId] = useState(null);
    const [quizResult, setQuizResult] = useState(null);
    const [loading, setLoading] = useState(true);
    const [reviews, setReviews] = useState([]);
    const [userRating, setUserRating] = useState(10);
    const [userComment, setUserComment] = useState("");
    const [isSubmittingReview, setIsSubmittingReview] = useState(false);
    const [userHasReviewed, setUserHasReviewed] = useState(false);
    const [inWishlist, setInWishlist] = useState(false);
    const [wishlistLoading, setWishlistLoading] = useState(false);

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

                    /* ===== QUIZ RESULT ===== */
                    try {
                        const quizRes = await quizApi.getUserResult(courseId);
                        setQuizResult(quizRes.data);
                    } catch {
                        setQuizResult(null);
                    }
                } else {
                    setEnrolled(false);
                }
            } catch {
                setEnrolled(false);
            }

            /* ===== REVIEWS ===== */
            try {
                const reviewsRes = await reviewApi.getCourseReviews(courseId);
                setReviews(reviewsRes.data);
            } catch (err) {
                console.error("Failed to load reviews", err);
            }

            /* ===== WISHLIST STATUS ===== */
            try {
                const wishRes = await checkWishlistStatus(courseId);
                setInWishlist(wishRes.data.inWishlist);
            } catch {
                setInWishlist(false);
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
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
    const handlePrimaryAction = async () => {
        if (!enrolled) {
            try {
                await enrollCourse(courseId);
                navigate(`/course/${courseId}/payment`);
            } catch (err) {
                console.error("Enrollment failed", err);
                alert("Enrollment failed. Please try again.");
            }
            return;
        }
        if (!nextLecture) return;
        navigate(`/course/${courseId}/lecture/${nextLecture.id}`);
    };

    const handleDownloadCertificate = async () => {
        try {
            const res = await downloadCertificate(courseId);
            const url = window.URL.createObjectURL(new Blob([res.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", `Certificate-${course.title}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (err) {
            console.error("Certificate download failed", err);
            alert("Failed to download certificate. Make sure you passed the quiz.");
        }
    };

    const handleReviewSubmit = async (e) => {
        e.preventDefault();
        if (!userRating || !userComment) return alert("Please provide both rating and comment");

        setIsSubmittingReview(true);
        try {
            await reviewApi.addReview({
                courseId: parseInt(courseId),
                rating: userRating,
                comment: userComment
            });
            alert("Review submitted successfully!");
            setUserComment("");
            setUserHasReviewed(true);
            load(); // Reload to get updated stats and review list
        } catch (err) {
            alert(err.response?.data?.message || "Failed to submit review");
        } finally {
            setIsSubmittingReview(false);
        }
    };

    const handleWishlistToggle = async () => {
        setWishlistLoading(true);
        try {
            const res = await toggleWishlist(courseId);
            setInWishlist(!inWishlist);
            // alert(res.data.message);
        } catch (err) {
            console.error("Wishlist toggle failed", err);
        } finally {
            setWishlistLoading(false);
        }
    };

    return (
        <div className="course-details">
            <div className="course-header-stats">
                <h2>{course.title}</h2>
                <div className="course-rating-summary">
                    <span className="rating-avg">⭐ {course.averageRating?.toFixed(1) || "0.0"}</span>
                    <span className="rating-count">({course.totalRatings || 0} students rated)</span>
                </div>
            </div>
            <p className="course-desc">{course.description}</p>

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
            <div className="cta-container">
                <button className="start-btn" onClick={handlePrimaryAction}>
                    {!enrolled
                        ? "Enroll Now"
                        : !resumeLectureId
                            ? "▶ Start Course"
                            : progress === 100
                                ? "✔ Course Completed"
                                : "▶ Continue Course"}
                </button>

                {!enrolled && (
                    <button 
                        className={`wishlist-btn ${inWishlist ? "active" : ""}`} 
                        onClick={handleWishlistToggle}
                        disabled={wishlistLoading}
                        title={inWishlist ? "Remove from Wishlist" : "Add to Wishlist"}
                    >
                        <Heart size={24} fill={inWishlist ? "currentColor" : "none"} />
                    </button>
                )}

                {enrolled && progress === 100 && (
                    <div className="completed-actions">
                        {!quizResult || !quizResult.passed ? (
                            <button
                                className="quiz-btn"
                                onClick={() => navigate(`/course/${courseId}/quiz`)}
                            >
                                {quizResult ? "Re-take Quiz" : "Take Course Quiz"}
                            </button>
                        ) : (
                            <button className="cert-btn" onClick={handleDownloadCertificate}>
                                Download Certificate
                            </button>
                        )}

                        {quizResult && (
                            <div className="quiz-status">
                                Last Score: {quizResult.score}/20 (
                                {quizResult.passed ? "PASSED" : "FAILED"})
                            </div>
                        )}
                    </div>
                )}
            </div>

            <div className="details-grid">
                <div className="lectures-section">
                    <h3>Course Lectures</h3>
                    <ul className="lecture-list">
                        {lectures.map((lecture, index) => {
                            const locked = !enrolled || index > currentIndex + 1;
                            const isDone = enrolled && currentIndex !== -1 && index <= currentIndex;
                            const isNext = enrolled && index === currentIndex + 1;

                            return (
                                <li
                                    key={lecture.id}
                                    className={`lecture-item ${locked ? "locked" : ""} ${isDone ? "completed" : ""}`}
                                    onClick={() => !locked && navigate(`/course/${courseId}/lecture/${lecture.id}`)}
                                >
                                    <div className="lecture-title-row">
                                        {isDone && <span className="done-icon">✔</span>}
                                        {lecture.title}
                                    </div>
                                    {isNext && enrolled && <span className="resume-badge">Next</span>}
                                    {locked && <span> 🔒</span>}
                                </li>
                            );
                        })}
                    </ul>
                </div>

                <div className="reviews-section">
                    {/* ===== REVIEW FORM ===== */}
                    {enrolled && progress === 100 && quizResult?.passed && !userHasReviewed && (
                        <div className="review-submission-section">
                            <h3>Rate this Course</h3>
                            <form onSubmit={handleReviewSubmit} className="review-form">
                                <div className="rating-input">
                                    <label>Rating (1-10):</label>
                                    <div className="rating-selector">
                                        {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((num) => (
                                            <button
                                                key={num}
                                                type="button"
                                                className={userRating === num ? "selected" : ""}
                                                onClick={() => setUserRating(num)}
                                            >
                                                {num}
                                            </button>
                                        ))}
                                    </div>
                                </div>
                                <textarea
                                    placeholder="Share your learning experience..."
                                    value={userComment}
                                    onChange={(e) => setUserComment(e.target.value)}
                                    required
                                />
                                <button type="submit" disabled={isSubmittingReview}>
                                    {isSubmittingReview ? "Submitting..." : "Post Review"}
                                </button>
                            </form>
                        </div>
                    )}

                    {/* ===== REVIEWS LIST ===== */}
                    <div className="course-reviews-list">
                        <h3>Student Reviews</h3>
                        {reviews.length === 0 ? (
                            <p className="no-reviews">No reviews yet. Be the first to rate!</p>
                        ) : (
                            <div className="reviews-stack">
                                {reviews.map((rev, idx) => (
                                    <div key={idx} className="review-card">
                                        <div className="review-header">
                                            <span className="rev-name">{rev.studentName}</span>
                                            <span className="rev-rating">⭐ {rev.rating}/10</span>
                                        </div>
                                        <p className="rev-comment">"{rev.comment}"</p>
                                        <span className="rev-date">{new Date(rev.createdAt).toLocaleDateString()}</span>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CourseDetails;
