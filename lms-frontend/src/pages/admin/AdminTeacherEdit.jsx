import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAdminTeacherById, updateAdminTeacher } from "../../api/adminApi";

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
    <>
      <h2>Edit Teacher</h2>

      <input
        name="name"
        placeholder="Name"
        value={form.name}
        onChange={handleChange}
      />

      <input
        name="email"
        placeholder="Email"
        value={form.email}
        onChange={handleChange}
      />

      <textarea
        name="bio"
        placeholder="Bio"
        value={form.bio}
        onChange={handleChange}
      />

      <div style={{ marginTop: 16, display: "flex", gap: 8 }}>
        <button onClick={handleSave}>Save</button>
        <button onClick={() => navigate(-1)}>Cancel</button>
      </div>
    </>
  );
};

export default AdminTeacherEdit;
