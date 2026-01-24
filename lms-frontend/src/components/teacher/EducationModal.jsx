import React, { useState, useEffect } from "react";
import "./EducationModal.css";

const EducationModal = ({ initialData, onClose, onSave }) => {
  const [form, setForm] = useState({
    institution: "",
    degree: "",
    major: "",
    startDate: "",
    endDate: "",
  });

  const [error, setError] = useState("");

  /* ================= PREFILL FOR EDIT ================= */
  useEffect(() => {
    if (initialData) {
      setForm({
        institution: initialData.institution || "",
        degree: initialData.degree || "",
        major: initialData.major || "",
        startDate: initialData.startDate || "",
        endDate: initialData.endDate || "",
      });
    }
  }, [initialData]);

  /* ================= CHANGE ================= */
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  /* ================= SUBMIT ================= */
  const handleSubmit = () => {
    if (!form.institution || !form.degree || !form.startDate) {
      setError("Institution, degree and start date are required");
      return;
    }

    if (form.endDate && form.endDate < form.startDate) {
      setError("End date cannot be before start date");
      return;
    }

    onSave(form); // 🔥 THIS NOW WORKS
  };

  return (
    <div className="modal-backdrop">
      <div className="edu-modal">
        <h3>{initialData ? "Edit Education" : "Add Education"}</h3>

        <input
          name="institution"
          placeholder="Institution"
          value={form.institution}
          onChange={handleChange}
        />

        <input
          name="degree"
          placeholder="Degree"
          value={form.degree}
          onChange={handleChange}
        />

        <input
          name="major"
          placeholder="Major"
          value={form.major}
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

        <div className="actions">
          <button
            type="button" // ✅ IMPORTANT
            className="cancel"
            onClick={onClose}
          >
            Cancel
          </button>

          <button
            type="button" // ✅ IMPORTANT
            className="save"
            onClick={handleSubmit}
          >
            Save
          </button>
        </div>
      </div>
    </div>
  );
};

export default EducationModal;
