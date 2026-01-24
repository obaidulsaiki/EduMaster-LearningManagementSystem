import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAdminTeacherById } from "../../api/adminApi";

const AdminTeacherView = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [teacher, setTeacher] = useState(null);

  useEffect(() => {
    getAdminTeacherById(id).then((res) => setTeacher(res.data));
  }, [id]);

  if (!teacher) return <p>Loading...</p>;

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
            {e.degree} – {e.institute} ({e.startYear}–{e.endYear || "Present"})
          </p>
        ))
      ) : (
        <p>No education info</p>
      )}

      <h3>Experience</h3>
      {teacher.experiences?.length ? (
        teacher.experiences.map((ex) => (
          <p key={ex.id}>
            {ex.role} @ {ex.company} ({ex.startYear}–{ex.endYear || "Present"})
          </p>
        ))
      ) : (
        <p>No experience info</p>
      )}

      <button onClick={() => navigate(-1)}>← Back</button>
    </>
  );
};

export default AdminTeacherView;
