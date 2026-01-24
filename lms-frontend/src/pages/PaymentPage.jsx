import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { confirmPayment } from "../api/enrollmentApi";
import "./PaymentPage.css";

const PaymentPage = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handlePay = async () => {
    try {
      setLoading(true);
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
      <div className="payment-card">
        <h2>Confirm Payment</h2>

        <p>This is a demo payment system.</p>

        <div className="fake-card">
          <input
            placeholder="Card Number"
            disabled
            value="4242 4242 4242 4242"
          />
          <input placeholder="MM/YY" disabled value="12/30" />
          <input placeholder="CVV" disabled value="123" />
        </div>

        <button onClick={handlePay} disabled={loading}>
          {loading ? "Processing..." : "Pay & Enroll"}
        </button>
      </div>
    </div>
  );
};

export default PaymentPage;
