import React, { useState } from "react";
import "./SettingsModal.css";

const ChangePasswordModal = ({ onClose, onSave }) => {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [error, setError] = useState(null);

  const handleSubmit = () => {
    if (newPassword.length < 6) {
      setError("Password must be at least 6 characters");
      return;
    }
    onSave({ oldPassword, newPassword });
  };

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <h3>Change Password</h3>

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

        {error && <p className="error">{error}</p>}

        <div className="actions">
          <button className="cancel" onClick={onClose}>
            Cancel
          </button>
          <button onClick={handleSubmit}>Update</button>
        </div>
      </div>
    </div>
  );
};

export default ChangePasswordModal;
