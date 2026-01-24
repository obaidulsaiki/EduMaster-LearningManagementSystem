import React, { useEffect, useState } from "react";

const EducationForm = ({ initialData, onSubmit, onCancel }) => {
  const [form, setForm] = useState({
    type: "",
    instituteName: "",
    department: "",
    degree: "",
    group: "",
    startYear: "",
    endYear: "",
    gpa: "",
  });

  useEffect(() => {
    if (initialData) {
      setForm({
        type: initialData.type || "",
        instituteName: initialData.instituteName || "",
        department: initialData.department || "",
        degree: initialData.degree || "",
        group: initialData.group || "",
        startYear: initialData.startYear || "",
        endYear: initialData.endYear || "",
        gpa: initialData.gpa || "",
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // ✅ MUST be form submit
  const handleSubmit = (e) => {
    e.preventDefault(); // 🔥 REQUIRED
    console.log("Submitting education:", form); // DEBUG

    onSubmit(form);
  };

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 60 }, (_, i) => currentYear - i);

  return (
    <form className="education-form" onSubmit={handleSubmit}>
      <h3>{initialData ? "Edit Education" : "Add Education"}</h3>

      <select name="type" value={form.type} onChange={handleChange} required>
        <option value="">Select Type</option>
        <option value="SSC">SSC</option>
        <option value="HSC">HSC</option>
        <option value="Bachelor">Bachelor</option>
        <option value="Master">Master</option>
        <option value="PhD">PhD</option>
      </select>

      <input
        name="instituteName"
        placeholder="Institute Name"
        value={form.instituteName}
        onChange={handleChange}
        required
      />

      <input
        name="department"
        placeholder="Department"
        value={form.department}
        onChange={handleChange}
      />

      <input
        name="degree"
        placeholder="Degree"
        value={form.degree}
        onChange={handleChange}
      />

      <input
        name="group"
        placeholder="Group"
        value={form.group}
        onChange={handleChange}
      />

      <select
        name="startYear"
        value={form.startYear}
        onChange={handleChange}
        required
      >
        <option value="">Start Year</option>
        {years.map((y) => (
          <option key={y} value={y}>
            {y}
          </option>
        ))}
      </select>

      <select name="endYear" value={form.endYear} onChange={handleChange}>
        <option value="">Present</option>
        {years.map((y) => (
          <option key={y} value={y}>
            {y}
          </option>
        ))}
      </select>

      <input
        name="gpa"
        placeholder="GPA"
        value={form.gpa}
        onChange={handleChange}
      />

      {/* ✅ IMPORTANT PART */}
      <div className="actions">
        <button type="button" onClick={onCancel}>
          Cancel
        </button>

        <button type="submit">Save</button>
      </div>
    </form>
  );
};

export default EducationForm;
