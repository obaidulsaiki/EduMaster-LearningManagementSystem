import React, { useState } from "react";
import { requestVerification } from "../../api/settingsApi";
import "./SettingsModal.css";

const ChangeEmailModal = ({ onClose, onSave }) => {
  const [email, setEmail] = useState("");
  const [code, setCode] = useState("");
  const [step, setStep] = useState(1); // 1: Email, 2: Verification Code
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSendCode = async () => {
    if (!email.includes("@")) {
      setError("Enter a valid email");
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await requestVerification("CHANGE_EMAIL");
      setStep(2);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to send verification code");
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = () => {
    if (code.length !== 6) {
      setError("Enter the 6-digit code");
      return;
    }
    onSave({ email, code });
  };

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <h3>Change Email</h3>

        {step === 1 ? (
          <>
            <p>Step 1: Enter your new email address</p>
            <input
              type="email"
              placeholder="New email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </>
        ) : (
          <>
            <p>Step 2: Enter the code sent to your current email</p>
            <input
              type="text"
              placeholder="6-digit code"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              maxLength="6"
            />
          </>
        )}

        {error && <p className="error">{error}</p>}

        <div className="actions">
          <button className="cancel" onClick={onClose} disabled={loading}>
            Cancel
          </button>
          {step === 1 ? (
            <button onClick={handleSendCode} disabled={loading}>
              {loading ? "Sending..." : "Send Code"}
            </button>
          ) : (
            <button onClick={handleSubmit} disabled={loading}>
              Update Email
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default ChangeEmailModal;
