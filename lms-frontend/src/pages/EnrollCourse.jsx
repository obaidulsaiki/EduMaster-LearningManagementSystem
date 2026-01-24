import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { enrollCourse, confirmPayment } from "../api/studentEnrollmentApi";
import "./EnrollCourse.css";

const EnrollCourse = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handlePayment = async () => {
    try {
      setLoading(true);

      // STEP 1: create enrollment
      await enrollCourse(courseId);

      // STEP 2: fake payment success
      await confirmPayment(courseId);

      navigate(`/course/${courseId}`);
    } catch {
      alert("Payment failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="payment-page">
      <h2>Confirm Enrollment</h2>
      <p>This is a demo payment system.</p>

      <button onClick={handlePayment} disabled={loading}>
        {loading ? "Processing..." : "Pay & Enroll"}
      </button>
    </div>
  );
};

export default EnrollCourse;
