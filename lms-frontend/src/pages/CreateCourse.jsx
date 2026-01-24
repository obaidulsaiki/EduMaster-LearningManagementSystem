import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createCourse } from "../api/teacherCourseApi";
import "./CreateCourse.css";

const CreateCourse = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    description: "",
    price: "",
    category: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async () => {
    if (!form.title || !form.category) {
      setError("Title and category are required");
      return;
    }

    try {
      setLoading(true);

      await createCourse({
        title: form.title,
        description: form.description,
        price: form.price || 0,
        category: form.category,
      });

      // ✅ After creation → manage lectures
      navigate("/teacher/courses");
    } catch (err) {
      setError("Failed to create course");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="create-course-page">
      <div className="create-course-card">
        <h2>Create New Course</h2>
        <p className="subtitle">
          Start with basic information. You can add lectures later.
        </p>

        <input
          name="title"
          placeholder="Course Title *"
          value={form.title}
          onChange={handleChange}
        />

        <textarea
          name="description"
          placeholder="Short Description"
          value={form.description}
          onChange={handleChange}
        />

        <input
          name="price"
          type="number"
          placeholder="Price (0 for free)"
          value={form.price}
          onChange={handleChange}
        />

        <select name="category" value={form.category} onChange={handleChange}>
          <option value="">Select Category *</option>
          <option value="AI">AI</option>
          <option value="Web">Web Development</option>
          <option value="Data">Data Science</option>
          <option value="Programming">Programming</option>
        </select>

        {error && <p className="error-text">{error}</p>}

        <div className="actions">
          <button className="btn-secondary" onClick={() => navigate(-1)}>
            Cancel
          </button>

          <button
            className="btn-primary"
            onClick={handleSubmit}
            disabled={loading}
          >
            {loading ? "Creating..." : "Create Course"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default CreateCourse;
