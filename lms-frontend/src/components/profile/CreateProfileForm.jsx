import React, { useState } from "react";
import "./CreateProfileForm.css";
import { createStudentProfile } from "../../api/profileApi";

const CreateProfileForm = ({ onSuccess }) => {
  const [form, setForm] = useState({
    name: "",
    bio: "",
    phone: "",
    location: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await createStudentProfile(form);
      onSuccess(res.data); // 🔥 updates Profile page instantly
    } catch (err) {
      setError("Failed to create profile. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="create-profile">
      <h2>Create Your Profile</h2>
      <p className="subtitle">
        Complete your profile to unlock courses & progress tracking
      </p>

      <form onSubmit={handleSubmit}>
        <label>
          Full Name
          <input
            name="name"
            value={form.name}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Bio
          <textarea
            name="bio"
            value={form.bio}
            onChange={handleChange}
            placeholder="Tell us about yourself"
          />
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

        <button disabled={loading}>
          {loading ? "Saving..." : "Save Profile"}
        </button>
      </form>
    </div>
  );
};

export default CreateProfileForm;
