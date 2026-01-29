import React, { useState } from "react";
import { requestVerification } from "../../api/settingsApi";
import "./SettingsModal.css";

const ChangePasswordModal = ({ onClose, onSave }) => {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [code, setCode] = useState("");
  const [step, setStep] = useState(1); // 1: Passwords, 2: Verification Code
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSendCode = async () => {
    if (newPassword.length < 6) {
      setError("Password must be at least 6 characters");
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await requestVerification("CHANGE_PASSWORD");
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
    onSave({ oldPassword, newPassword, code });
  };

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <h3>Update Password</h3>

        {step === 1 ? (
          <>
            <p>Step 1: Enter current and new password</p>
            <input
              type="password"
              placeholder="Current password"
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
            />
            <input
              type="password"
              placeholder="New password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </>
        ) : (
          <>
            <p>Step 2: Enter the code sent to your email</p>
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
              Update Password
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default ChangePasswordModal;
