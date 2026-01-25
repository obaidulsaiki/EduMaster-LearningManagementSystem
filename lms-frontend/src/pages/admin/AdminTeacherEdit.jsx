import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAdminTeacherById, updateAdminTeacher } from "../../api/adminApi";
import "./AdminTeacherEdit.css";

const AdminTeacherEdit = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    email: "",
    bio: "",
  });

  useEffect(() => {
    getAdminTeacherById(id).then((res) => {
      setForm({
        name: res.data.name,
        email: res.data.email,
        bio: res.data.bio || "",
        enabled: res.data.enabled
      });
    });
  }, [id]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSave = async () => {
    await updateAdminTeacher(id, form);
    alert("Teacher updated");
    navigate(-1);
  };

  return (
    <div className="edit-teacher-container">
      <h2>Edit Teacher</h2>

      <div className="form-group">
        <label>Full Name</label>
        <input
          name="name"
          placeholder="Name"
          value={form.name}
          onChange={handleChange}
        />
      </div>

      <div className="form-group">
        <label>Email Address</label>
        <input
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
        />
      </div>

      <div className="form-group">
        <label>Description / Bio</label>
        <textarea
          name="bio"
          placeholder="Bio"
          value={form.bio}
          onChange={handleChange}
        />
      </div>

      <div className="edit-actions">
        <button className="btn-primary" onClick={handleSave}>Save Changes</button>
        <button className="btn-secondary" onClick={() => navigate(-1)}>Cancel</button>
      </div>
    </div>
  );
};

export default AdminTeacherEdit;
