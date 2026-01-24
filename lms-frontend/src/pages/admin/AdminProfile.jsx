import React, { useEffect, useState } from "react";
import { getAdminProfile, updateAdminProfile } from "../../api/adminProfileApi";
import "./AdminProfile.css";

const AdminProfile = () => {
  // 'profile' stores the committed data from the server
  const [profile, setProfile] = useState(null);

  // 'formData' stores the temporary changes while editing
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    designation: "",
  });

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [status, setStatus] = useState({ type: "", message: "" });

  useEffect(() => {
    getAdminProfile()
      .then((res) => {
        setProfile(res.data);
        setFormData(res.data); // Initialize form data
      })
      .catch((err) => console.error("Failed to load profile", err))
      .finally(() => setLoading(false));
  }, []);

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Switch to Edit Mode
  const handleEditClick = () => {
    setStatus({ type: "", message: "" });
    setIsEditing(true);
  };

  // Cancel Editing (Revert to original profile data)
  const handleCancel = () => {
    setFormData(profile); // Reset form to match profile
    setIsEditing(false);
    setStatus({ type: "", message: "" });
  };

  // Save Changes
  const handleSave = async () => {
    setSaving(true);
    setStatus({ type: "", message: "" });

    try {
      // Sending the whole formData object now, not just designation
      const res = await updateAdminProfile(formData);

      setProfile(res.data); // Update source of truth
      setFormData(res.data); // Sync form data
      setIsEditing(false); // Exit edit mode

      setStatus({ type: "success", message: "Profile updated successfully!" });
      setTimeout(() => setStatus({ type: "", message: "" }), 3000);
    } catch (error) {
      console.error(error);
      setStatus({ type: "error", message: "Failed to update profile." });
    } finally {
      setSaving(false);
    }
  };

  const getInitials = (name) => {
    return name
      ? name
          .split(" ")
          .map((n) => n[0])
          .join("")
          .toUpperCase()
          .slice(0, 2)
      : "AD";
  };

  if (loading)
    return (
      <div className="loading-container">
        <div className="spinner"></div>
      </div>
    );

  return (
    <div className="admin-profile-page">
      <div className="profile-container">
        {/* Header */}
        <div className="profile-header">
          <div className="avatar-circle">{getInitials(profile?.name)}</div>
          <div className="header-text">
            <h2>{profile?.name}</h2>
            <span className="role-badge">
              {profile?.designation || "Administrator"}
            </span>
          </div>
        </div>

        {/* Body */}
        <div className="profile-body">
          <div className="profile-fields">
            {/* Name Field */}
            <div className="field-group">
              <label>Full Name</label>
              {isEditing ? (
                <input
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  className="input-edit"
                />
              ) : (
                <div className="value-display">{profile.name}</div>
              )}
            </div>

            {/* Email Field */}
            <div className="field-group">
              <label>Email Address</label>
              {isEditing ? (
                <input
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className="input-edit"
                />
              ) : (
                <div className="value-display">{profile.email}</div>
              )}
            </div>

            {/* Designation Field */}
            <div className="field-group">
              <label>Designation</label>
              {isEditing ? (
                <input
                  name="designation"
                  value={formData.designation}
                  onChange={handleChange}
                  className="input-edit"
                  placeholder="e.g. System Admin"
                />
              ) : (
                <div className="value-display">
                  {profile.designation || (
                    <span className="text-muted">Not Set</span>
                  )}
                </div>
              )}
            </div>
          </div>

          {/* Status Messages */}
          {status.message && (
            <div className={`status-message ${status.type}`}>
              {status.message}
            </div>
          )}

          {/* Action Buttons */}
          <div className="action-footer">
            {!isEditing ? (
              <button className="btn-primary" onClick={handleEditClick}>
                Edit Profile
              </button>
            ) : (
              <div className="edit-actions">
                <button
                  className="btn-secondary"
                  onClick={handleCancel}
                  disabled={saving}
                >
                  Cancel
                </button>
                <button
                  className="btn-primary"
                  onClick={handleSave}
                  disabled={saving}
                >
                  {saving ? "Saving..." : "Save Changes"}
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminProfile;
