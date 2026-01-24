import React, { useState } from "react";
import "./SettingsModal.css";

const ChangeEmailModal = ({ onClose, onSave }) => {
  const [email, setEmail] = useState("");
  const [error, setError] = useState(null);

  const handleSubmit = () => {
    if (!email.includes("@")) {
      setError("Enter a valid email");
      return;
    }
    onSave(email);
  };

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <h3>Change Email</h3>

        <input
          type="email"
          placeholder="New email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
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

export default ChangeEmailModal;
