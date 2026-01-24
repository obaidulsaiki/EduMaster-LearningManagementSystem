import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../services/AuthService";
import { useAuth } from "../context/AuthContext";
import "./Auth.css";

function Login() {
  const navigate = useNavigate();
  const { login } = useAuth(); // ✅ context login

  const [form, setForm] = useState({
    role: "STUDENT",
    email: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await loginUser(form);

      // ✅ this updates Navbar instantly
      login(response);

      if (response.role === "ADMIN") navigate("/admin");
      else if (response.role === "TEACHER") navigate("/teacher/dashboard");
      else navigate("/");
    } catch (err) {
      setError(err.response?.data?.message || "Invalid email or password");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Welcome Back</h2>
        <p className="auth-subtitle">Login as Student, Teacher, or Admin</p>

        {error && <p className="auth-error">{error}</p>}

        {/* ROLE SELECTION */}
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

          <label className={form.role === "ADMIN" ? "active" : ""}>
            <input
              type="radio"
              name="role"
              value="ADMIN"
              checked={form.role === "ADMIN"}
              onChange={handleChange}
            />
            Admin
          </label>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          <label>Email</label>
          <input
            type="email"
            name="email"
            placeholder="you@example.com"
            value={form.email}
            onChange={handleChange}
            required
          />

          <label>Password</label>
          <input
            type="password"
            name="password"
            placeholder="••••••••"
            value={form.password}
            onChange={handleChange}
            required
          />

          <button type="submit" className="auth-btn">
            Login
          </button>
        </form>

        <p className="auth-footer">
          Don’t have an account? <Link to="/register">Register here</Link>
        </p>
      </div>
    </div>
  );
}

export default Login;
