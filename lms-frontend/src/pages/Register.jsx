import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser, checkEmailExists } from "../services/AuthService";
import "./Auth.css";

function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    role: "STUDENT",
    name: "",
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    /* ---------- FRONTEND VALIDATION ---------- */

    // 1️⃣ Password length check
    if (form.password.length < 8) {
      setError("Password must be at least 8 characters long");
      return;
    }

    try {
      setLoading(true);

      // 2️⃣ Email uniqueness check
      const emailExists = await checkEmailExists(form.email);
      if (emailExists) {
        setError("Email already exists. Please use another email.");
        setLoading(false);
        return;
      }

      // 3️⃣ Register user
      await registerUser(form);

      setSuccess("Registration successful! Redirecting to login...");
      setTimeout(() => navigate("/login"), 1200);
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Create Account</h2>
        <p className="auth-subtitle">Students and teachers can register</p>

        {error && <p className="auth-error">{error}</p>}
        {success && <p className="auth-success">{success}</p>}

        <div className="role-selector">
          <label className={form.role === "STUDENT" ? "active" : ""}>
            <input
              type="radio"
              name="role"
              value="STUDENT"
              checked={form.role === "STUDENT"}
              onChange={handleChange}
            />
            Student
          </label>

          <label className={form.role === "TEACHER" ? "active" : ""}>
            <input
              type="radio"
              name="role"
              value="TEACHER"
              checked={form.role === "TEACHER"}
              onChange={handleChange}
            />
            Teacher
          </label>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          <label>Full Name</label>
          <input
            type="text"
            name="name"
            value={form.name}
            onChange={handleChange}
            required
          />

          <label>Email</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
          />

          <label>Password</label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            required
          />

          <button type="submit" className="auth-btn" disabled={loading}>
            {loading ? "Registering..." : "Register"}
          </button>
        </form>

        <p className="auth-footer">
          Already registered? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
