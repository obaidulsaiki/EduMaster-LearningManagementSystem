import React, { useEffect, useState } from "react";
import "./LectureModal.css";

const LectureModal = ({ initialData, onClose, onSave }) => {
  const [form, setForm] = useState({
    title: "",
    videoUrl: "",
  });

  useEffect(() => {
    if (initialData) {
      setForm({
        title: initialData.title || "",
        videoUrl: initialData.videoUrl || "",
      });
    }
  }, [initialData]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = () => {
    if (!form.title || !form.videoUrl) return;
    onSave(form);
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-card">
        <h3>{initialData ? "Edit Lecture" : "Add Lecture"}</h3>

        <input
          name="title"
          placeholder="Lecture title"
          value={form.title}
          onChange={handleChange}
        />

        <input
          name="videoUrl"
          placeholder="Video URL"
          value={form.videoUrl}
          onChange={handleChange}
        />

        <div className="modal-actions">
          <button className="btn-cancel" onClick={onClose}>
            Cancel
          </button>
          <button className="btn-save" onClick={handleSubmit}>
            Save
          </button>
        </div>
      </div>
    </div>
  );
};

export default LectureModal;
