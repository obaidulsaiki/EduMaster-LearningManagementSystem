import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { confirmPayment } from "../api/studentEnrollmentApi";
import { getCourseDetails } from "../api/courseApi";
import { downloadBlob } from "../utils/downloadFile";
import api from "../api/api";
import "./PaymentPage.css";

const PaymentPage = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [course, setCourse] = useState(null);
  const [activeTab, setActiveTab] = useState("mobile"); // mobile | card
  const [selectedProvider, setSelectedProvider] = useState(null);
  const [isSuccess, setIsSuccess] = useState(false);

  useEffect(() => {
    getCourseDetails(courseId).then((res) => setCourse(res.data));
  }, [courseId]);

  const handlePay = async () => {
    if (!selectedProvider) {
      alert("Please select a payment method");
      return;
    }

    try {
      setLoading(true);
      // Simulate real gateway redirection delay
      await new Promise((r) => setTimeout(r, 2000));

      const txId =
        "TXN-" + Math.random().toString(36).substring(2, 11).toUpperCase();

      await confirmPayment(courseId, {
        transactionId: txId,
        method: selectedProvider,
        amount: course.price,
      });

      setLoading(false);
      setIsSuccess(true);

      // 🔥 DOWNLOAD INVOICE
      try {
        const invRes = await api.get(`/student/invoices/${courseId}/download`, {
          responseType: "blob",
        });
        downloadBlob(
          invRes.data,
          `Invoice-${course.title.replace(/\s+/g, "-")}.pdf`,
        );
      } catch (err) {
        console.error("Invoice download failed", err);
      }

      // Auto-redirect after showing success
      setTimeout(() => {
        navigate(`/course/${courseId}`);
      }, 3500);
    } catch {
      setLoading(false);
      alert("Payment failed");
    }
  };

  if (!course)
    return (
      <div className="payment-page">
        <p>Loading payment details...</p>
      </div>
    );

  return (
    <div className="payment-page">
      {loading && (
        <div className="payment-loader-overlay">
          <div className="spinner"></div>
          <h3>Verifying Payment with SSLCommerz...</h3>
          <p>Please do not close this window</p>
        </div>
      )}

      {isSuccess && (
        <div className="payment-loader-overlay success">
          <div className="success-icon">✔</div>
          <h3>Payment Successfully Paid!</h3>
          <p>You have been enrolled in the course.</p>
          <p style={{ marginTop: 10, fontSize: "0.8rem", opacity: 0.8 }}>
            Redirecting to course details...
          </p>
        </div>
      )}

      {/* ===== GATEWAY MAIN ===== */}
      <div className="gateway-container">
        <header className="gateway-header">
          <div className="ssl-logo">
            SSL<span>Commerz</span>
          </div>
          <div className="merchant-info">
            <span style={{ fontSize: "0.8rem", color: "var(--text-muted)" }}>
              Merchant:
            </span>
            <div style={{ fontWeight: "700" }}>EduMaster LMS</div>
          </div>
        </header>

        <nav className="payment-tabs">
          <div
            className={`tab ${activeTab === "mobile" ? "active" : ""}`}
            onClick={() => setActiveTab("mobile")}
          >
            Mobile Banking
          </div>
          <div
            className={`tab ${activeTab === "card" ? "active" : ""}`}
            onClick={() => setActiveTab("card")}
          >
            Debit/Credit Card
          </div>
        </nav>

        <div className="payment-content">
          {activeTab === "mobile" ? (
            <div className="provider-grid">
              <ProviderCard
                name="bKash"
                color="#e2136e"
                selected={selectedProvider === "bkash"}
                onClick={() => setSelectedProvider("bkash")}
              />
              <ProviderCard
                name="Nagad"
                color="#f7941d"
                selected={selectedProvider === "nagad"}
                onClick={() => setSelectedProvider("nagad")}
              />
              <ProviderCard
                name="Rocket"
                color="#8c3394"
                selected={selectedProvider === "rocket"}
                onClick={() => setSelectedProvider("rocket")}
              />
              <ProviderCard
                name="Upay"
                color="#ffda00"
                selected={selectedProvider === "upay"}
                onClick={() => setSelectedProvider("upay")}
              />
            </div>
          ) : (
            <div className="provider-grid">
              <ProviderCard
                name="Visa"
                color="#1a1f71"
                selected={selectedProvider === "visa"}
                onClick={() => setSelectedProvider("visa")}
              />
              <ProviderCard
                name="Mastercard"
                color="#eb001b"
                selected={selectedProvider === "mastercard"}
                onClick={() => setSelectedProvider("mastercard")}
              />
              <ProviderCard
                name="UnionPay"
                color="#006c9e"
                selected={selectedProvider === "unionpay"}
                onClick={() => setSelectedProvider("unionpay")}
              />
            </div>
          )}
        </div>

        <footer className="gateway-footer">
          <div className="total-amount">
            <span>Total Payable</span>
            <strong>${course.price}</strong>
          </div>
          <button className="pay-btn" onClick={handlePay} disabled={loading}>
            Proceed to Pay
          </button>
        </footer>
      </div>

      {/* ===== SUMMARY ===== */}
      <aside className="order-summary">
        <h3>Order Summary</h3>
        <div className="summary-item">
          <span>Course Name</span>
          <span
            style={{ fontWeight: "600", maxWidth: "150px", textAlign: "right" }}
          >
            {course.title}
          </span>
        </div>
        <div className="summary-item">
          <span>Amount</span>
          <span>${course.price}</span>
        </div>
        <div className="summary-item">
          <span>Vat (0%)</span>
          <span>$0.00</span>
        </div>
        <div className="summary-total">
          <span>Total</span>
          <span>${course.price}</span>
        </div>

        <div
          style={{
            marginTop: 30,
            textAlign: "center",
            fontStyle: "italic",
            fontSize: "0.8rem",
            color: "var(--text-muted)",
          }}
        >
          <p>
            Secure payment provided by SSLCommerz. Your data is encrypted and
            safe.
          </p>
        </div>
      </aside>
    </div>
  );
};

const ProviderCard = ({ name, color, selected, onClick }) => (
  <div
    className={`provider-card ${selected ? "selected" : ""}`}
    onClick={onClick}
  >
    <div
      style={{
        width: 44,
        height: 44,
        background: color || "var(--bg-muted)",
        borderRadius: "12px",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontWeight: "900",
        fontSize: "0.6rem",
        color: "white",
        boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
      }}
    >
      {name.substring(0, 3).toUpperCase()}
    </div>
    <span className="provider-name">{name}</span>
  </div>
);

export default PaymentPage;
