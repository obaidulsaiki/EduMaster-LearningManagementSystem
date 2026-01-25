import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAdminTeacherById } from "../../api/adminApi";

const AdminTeacherView = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [teacher, setTeacher] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    getAdminTeacherById(id)
      .then((res) => setTeacher(res.data))
      .catch((err) => {
        console.error("Failed to load teacher", err);
        setError("Teacher not found or server error");
      });
  }, [id]);

  if (error) return <p className="error-text">{error}</p>;
  if (!teacher || teacher.id.toString() !== id.toString()) return <p>Loading...</p>;

  return (
    <>
      <h2>Teacher Details</h2>

      <p>
        <b>Name:</b> {teacher.name}
      </p>
      <p>
        <b>Email:</b> {teacher.email}
      </p>
      <p>
        <b>Bio:</b> {teacher.bio || "—"}
      </p>

      <h3>Education</h3>
      {teacher.educations?.length ? (
        teacher.educations.map((e) => (
          <p key={e.id}>
            {e.degree} in {e.major} – {e.institution} ({e.startDate ? new Date(e.startDate).getFullYear() : ""} – {e.endDate ? new Date(e.endDate).getFullYear() : "Present"})
          </p>
        ))
      ) : (
        <p>No education info</p>
      )}

      <h3>Experience</h3>
      {teacher.experiences?.length ? (
        teacher.experiences.map((ex) => (
          <p key={ex.id}>
            {ex.designation} @ {ex.company} ({ex.startDate ? new Date(ex.startDate).getFullYear() : ""} – {ex.endDate ? new Date(ex.endDate).getFullYear() : "Present"})
          </p>
        ))
      ) : (
        <p>No experience info</p>
      )}

      <button className="btn-secondary" onClick={() => navigate(-1)} style={{marginTop: 20}}>
        ← Back
      </button>
    </>
  );
};

export default AdminTeacherView;
