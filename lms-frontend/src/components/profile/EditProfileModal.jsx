import React, { useState } from "react";
import "./EditProfileModal.css";
import { updateStudentProfile, getStudentProfile } from "../../api/profileApi";

const EditProfileModal = ({ profile, onClose, onUpdate }) => {
  const [form, setForm] = useState({
    name: profile.name || "",
    bio: profile.bio || "",
    phone: profile.phone || "",
    location: profile.location || "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSave = async () => {
    setLoading(true);
    setError(null);

    try {
      // ✅ update profile
      await updateStudentProfile(form);

      // 🔥 re-fetch full profile
      const freshProfile = await getStudentProfile();

      // ✅ update parent with COMPLETE data
      onUpdate(freshProfile.data);

      onClose();
    } catch (err) {
      console.error(err);
      setError("Failed to update profile");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <h3>Edit Profile</h3>

        <label>
          Full Name
          <input name="name" value={form.name} onChange={handleChange} />
        </label>

        <label>
          Bio
          <textarea name="bio" value={form.bio} onChange={handleChange} />
        </label>

        <label>
          Phone
          <input name="phone" value={form.phone} onChange={handleChange} />
        </label>

        <label>
          Location
          <input
            name="location"
            value={form.location}
            onChange={handleChange}
          />
        </label>

        {error && <p className="error">{error}</p>}

        <div className="actions">
          <button className="cancel" onClick={onClose}>
            Cancel
          </button>
          <button onClick={handleSave} disabled={loading}>
            {loading ? "Saving..." : "Save"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default EditProfileModal;
