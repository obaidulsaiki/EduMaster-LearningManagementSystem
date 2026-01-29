import React, { useState, useEffect } from "react";
import "./Settings.css";

import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { logout } from "../auth/auth";

import {
  updateEmail,
  updatePassword,
  updatePreferences,
  deleteAccount,
} from "../api/settingsApi";

import ChangeEmailModal from "../components/settings/ChangeEmailModal";
import ChangePasswordModal from "../components/settings/ChangePasswordModal";

const Settings = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  /* ================= STATE ================= */

  const [darkMode, setDarkMode] = useState(
    localStorage.getItem("theme") !== "light"
  );

  const [emailNotifications, setEmailNotifications] = useState(true);

  const [showEmailModal, setShowEmailModal] = useState(false);
  const [showPasswordModal, setShowPasswordModal] = useState(false);

  /* ================= THEME & PREFERENCES ================= */

  useEffect(() => {
    const theme = darkMode ? "dark" : "light";

    // ✅ UI only — safe for ALL roles
    document.body.classList.toggle("dark", theme === "dark");
    localStorage.setItem("theme", theme);

    // ✅ Backend sync — STUDENT ONLY
    if (user?.role === "STUDENT") {
      updatePreferences({
        darkMode,
        emailNotifications,
      }).catch(() => {});
    }
  }, [darkMode, emailNotifications, user?.role]);

  /* ================= PROFILE REDIRECT ================= */

  const handleEditProfile = () => {
    if (user?.role === "STUDENT") {
      navigate("/profile");
    } else if (user?.role === "TEACHER") {
      navigate("/teacher/profile");
    } else if (user?.role === "ADMIN") {
      navigate("/admin/profile");
    }
  };

  /* ================= DELETE ACCOUNT ================= */

  const handleDeleteAccount = async () => {
    const confirmDelete = window.confirm(
      "⚠️ This action is irreversible. Delete your account?"
    );

    if (!confirmDelete) return;

    try {
      await deleteAccount();
      logout();
    } catch {
      alert("Failed to delete account");
    }
  };

  /* ================= UI ================= */

  return (
    <div className="settings-page">
      <div className="settings-container">
        <h1 className="settings-title">Settings</h1>
        <p className="settings-subtitle">Manage your account preferences</p>

        {/* ================= ACCOUNT ================= */}
        <section className="settings-card">
          <h3>Account</h3>

          <div className="settings-row">
            <span>Email</span>
            <button
              className="btn-secondary"
              onClick={() => setShowEmailModal(true)}
            >
              Change
            </button>
          </div>

          <div className="settings-row">
            <span>Password</span>
            <button
              className="btn-secondary"
              onClick={() => setShowPasswordModal(true)}
            >
              Update
            </button>
          </div>
        </section>

        {/* ================= PROFILE ================= */}
        <section className="settings-card">
          <h3>Profile</h3>

          <div className="settings-row">
            <span>Edit personal information</span>
            <button className="btn-secondary" onClick={handleEditProfile}>
              Edit
            </button>
          </div>
        </section>

        {/* ================= PREFERENCES ================= */}
        <section className="settings-card">
          <h3>Preferences</h3>

          <div className="settings-row">
            <span>Dark Mode</span>
            <input
              type="checkbox"
              checked={darkMode}
              onChange={() => setDarkMode((prev) => !prev)}
            />
          </div>

          {/* 🔥 STUDENT ONLY */}
          {user?.role === "STUDENT" && (
            <div className="settings-row">
              <span>Email Notifications</span>
              <input
                type="checkbox"
                checked={emailNotifications}
                onChange={() => setEmailNotifications((prev) => !prev)}
              />
            </div>
          )}
        </section>

        {/* ================= DANGER ZONE ================= */}
        <section className="settings-card danger">
          <h3>Danger Zone</h3>

          <button className="btn-danger" onClick={handleDeleteAccount}>
            Delete Account
          </button>
        </section>
      </div>

      {/* ================= MODALS ================= */}

      {showEmailModal && (
        <ChangeEmailModal
          onClose={() => setShowEmailModal(false)}
          onSave={async (data) => {
            try {
              await updateEmail(data);
              alert("Email updated. Please login again.");
              logout();
            } catch (err) {
              alert(err.response?.data?.message || "Failed to update email");
            }
          }}
        />
      )}

      {showPasswordModal && (
        <ChangePasswordModal
          onClose={() => setShowPasswordModal(false)}
          onSave={async (data) => {
            try {
              await updatePassword(data);
              alert("Password updated. Please login again.");
              logout();
            } catch {
              alert("Failed to update password");
            }
          }}
        />
      )}
    </div>
  );
};

export default Settings;
