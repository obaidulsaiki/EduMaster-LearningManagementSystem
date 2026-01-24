import React, { useEffect, useState } from "react";
import "./ExperienceModal.css";

const ExperienceModal = ({ initialData, onClose, onSave }) => {
  const [form, setForm] = useState({
    company: "",
    designation: "",
    startDate: "",
    endDate: "",
  });

  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  /* ================= PREFILL (EDIT MODE) ================= */
  useEffect(() => {
    if (initialData) {
      setForm({
        company: initialData.company || "",
        designation: initialData.designation || "",
        startDate: initialData.startDate || "",
        endDate: initialData.endDate || "",
      });
    }
  }, [initialData]);

  /* ================= ESC KEY CLOSE ================= */
  useEffect(() => {
    const handleEsc = (e) => {
      if (e.key === "Escape") onClose();
    };
    window.addEventListener("keydown", handleEsc);
    return () => window.removeEventListener("keydown", handleEsc);
  }, [onClose]);

  /* ================= CHANGE ================= */
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    if (error) setError(""); // 🔥 auto clear error
  };

  /* ================= VALIDATION ================= */
  const isValid =
    form.company.trim() &&
    form.designation.trim() &&
    form.startDate &&
    (!form.endDate || form.endDate >= form.startDate);

  /* ================= SUBMIT ================= */
  const handleSubmit = async () => {
    if (!isValid) {
      setError("Please fill required fields correctly");
      return;
    }

    try {
      setSaving(true);
      await onSave(form);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-card" onClick={(e) => e.stopPropagation()}>
        <h3>{initialData ? "Edit Experience" : "Add Experience"}</h3>

        <input
          name="company"
          placeholder="Company Name *"
          value={form.company}
          onChange={handleChange}
        />

        <input
          name="designation"
          placeholder="Designation *"
          value={form.designation}
          onChange={handleChange}
        />

        <div className="date-row">
          <input
            type="date"
            name="startDate"
            value={form.startDate}
            onChange={handleChange}
          />
          <input
            type="date"
            name="endDate"
            value={form.endDate}
            onChange={handleChange}
          />
        </div>

        {error && <p className="error-text">{error}</p>}

        <div className="modal-actions">
          <button className="btn-cancel" onClick={onClose} disabled={saving}>
            Cancel
          </button>

          <button
            className="btn-save"
            onClick={handleSubmit}
            disabled={!isValid || saving}
          >
            {saving ? "Saving..." : "Save"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ExperienceModal;
