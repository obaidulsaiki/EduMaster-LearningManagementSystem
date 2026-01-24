import React, { useState } from "react";
import "./EditTeacherProfileModal.css";

const EditTeacherProfileModal = ({ profile, onClose, onSave }) => {
  const [form, setForm] = useState({
    bio: profile.bio || "",
    workingField: profile.workingField || "",
    experienceYears: profile.experienceYears || 0,
    linkedin: profile.linkedin || "",
  });

  const [saving, setSaving] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    try {
      setSaving(true);
      await onSave(form); // 🔥 MUST be awaited
      onClose(); // 🔥 Close modal after success
    } catch (err) {
      alert("Failed to update profile");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <h3>Edit Profile</h3>

        <input
          name="workingField"
          placeholder="Working Field"
          value={form.workingField}
          onChange={handleChange}
        />

        <input
          name="linkedin"
          placeholder="LinkedIn URL"
          value={form.linkedin}
          onChange={handleChange}
        />

        <input
          type="number"
          name="experienceYears"
          placeholder="Experience (years)"
          value={form.experienceYears}
          onChange={handleChange}
        />

        <textarea
          name="bio"
          placeholder="Short bio"
          value={form.bio}
          onChange={handleChange}
        />

        <div className="actions">
          <button className="cancel" onClick={onClose} disabled={saving}>
            Cancel
          </button>
          <button onClick={handleSubmit} disabled={saving}>
            {saving ? "Saving..." : "Save"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default EditTeacherProfileModal;
