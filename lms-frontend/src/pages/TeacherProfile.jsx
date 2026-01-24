import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";

import {
  getMyTeacherProfile,
  updateTeacherProfile,
  getMyTeacherEducations,
  addTeacherEducation,
  deleteTeacherEducation,
  updateTeacherEducation,
  getMyTeacherExperiences,
  addTeacherExperience,
  deleteTeacherExperience,
  updateTeacherExperience,
} from "../api/teacherApi";

import EditTeacherProfileModal from "../components/teacher/EditTeacherProfileModal";
import EducationModal from "../components/teacher/EducationModal";
import ExperienceModal from "../components/teacher/ExperienceModal";

import "./TeacherProfile.css";

const TeacherProfile = () => {
  const { user } = useAuth();

  const canEdit = user?.role === "TEACHER";

  const [profile, setProfile] = useState(null);
  const [educations, setEducations] = useState([]);
  const [experiences, setExperiences] = useState([]);
  const [loading, setLoading] = useState(true);

  const [showEditProfile, setShowEditProfile] = useState(false);
  const [showEduModal, setShowEduModal] = useState(false);
  const [showExpModal, setShowExpModal] = useState(false);

  const [editingEducation, setEditingEducation] = useState(null);
  const [editingExperience, setEditingExperience] = useState(null);

  /* ================= LOAD DATA ================= */
  useEffect(() => {
    const loadData = async () => {
      try {
        const profileRes = await getMyTeacherProfile();
        setProfile(profileRes.data);

        if (canEdit) {
          const eduRes = await getMyTeacherEducations();
          const expRes = await getMyTeacherExperiences();

          setEducations(eduRes.data || []);
          setExperiences(expRes.data || []);
        }
      } catch (err) {
        console.error("Failed to load teacher profile", err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [canEdit]);

  if (loading) return <p className="page-loading">Loading...</p>;
  if (!profile) return <p>Profile not found</p>;

  return (
    <div className="teacher-profile-page">
      {/* HEADER */}
      <div className="teacher-header">
        <div className="avatar">{profile.name?.charAt(0)}</div>

        <div className="info">
          <h2>{profile.name}</h2>
          <p className="field">{profile.workingField || "—"}</p>

          {profile.linkedin && (
            <a
              href={profile.linkedin}
              target="_blank"
              rel="noreferrer"
              className="linkedin"
            >
              LinkedIn
            </a>
          )}
        </div>

        {canEdit && (
          <button className="edit-btn" onClick={() => setShowEditProfile(true)}>
            Edit Profile
          </button>
        )}
      </div>

      {/* ABOUT */}
      <section className="profile-section">
        <h3>About</h3>
        <p className="bio">{profile.bio || "No bio provided."}</p>
      </section>

      {/* EDUCATION */}
      <section className="profile-section">
        <div className="section-header">
          <h3>Education</h3>
          {canEdit && (
            <button
              className="small-btn"
              onClick={() => {
                setEditingEducation(null);
                setShowEduModal(true);
              }}
            >
              + Add
            </button>
          )}
        </div>

        {educations.length === 0 ? (
          <p className="muted">No education added</p>
        ) : (
          educations.map((edu) => (
            <div key={edu.id} className="info-card">
              <div>
                <h4>
                  {edu.degree}
                  {edu.major ? ` – ${edu.major}` : ""}
                </h4>
                <p>{edu.institution}</p>
                <span>
                  {edu.startDate} – {edu.endDate || "Present"}
                </span>
              </div>

              {canEdit && (
                <div className="card-actions">
                  <button
                    className="edit-small"
                    onClick={() => {
                      setEditingEducation(edu);
                      setShowEduModal(true);
                    }}
                  >
                    Edit
                  </button>
                  <button
                    className="delete-btn"
                    onClick={async () => {
                      if (!window.confirm("Delete this education?")) return;
                      await deleteTeacherEducation(edu.id);
                      setEducations(educations.filter((e) => e.id !== edu.id));
                    }}
                  >
                    Delete
                  </button>
                </div>
              )}
            </div>
          ))
        )}
      </section>

      {/* EXPERIENCE */}
      <section className="profile-section">
        <div className="section-header">
          <h3>Experience</h3>
          {canEdit && (
            <button
              className="small-btn"
              onClick={() => {
                setEditingExperience(null);
                setShowExpModal(true);
              }}
            >
              + Add
            </button>
          )}
        </div>

        {experiences.length === 0 ? (
          <p className="muted">No experience added</p>
        ) : (
          experiences.map((exp) => (
            <div key={exp.id} className="info-card">
              <div>
                <h4>{exp.designation}</h4>
                <p>{exp.company}</p>
                <span>
                  {exp.startDate} – {exp.endDate || "Present"}
                </span>
              </div>

              {canEdit && (
                <div className="card-actions">
                  <button
                    className="edit-small"
                    onClick={() => {
                      setEditingExperience(exp);
                      setShowExpModal(true);
                    }}
                  >
                    Edit
                  </button>
                  <button
                    className="delete-btn"
                    onClick={async () => {
                      if (!window.confirm("Delete this experience?")) return;
                      await deleteTeacherExperience(exp.id);
                      setExperiences(
                        experiences.filter((e) => e.id !== exp.id)
                      );
                    }}
                  >
                    Delete
                  </button>
                </div>
              )}
            </div>
          ))
        )}
      </section>

      {/* COURSES */}
      <section className="profile-section">
        <h3>Courses</h3>
        <p className="muted">Published courses will appear here</p>
      </section>

      {/* MODALS */}
      {showEditProfile && (
        <EditTeacherProfileModal
          profile={profile}
          onClose={() => setShowEditProfile(false)}
          onSave={async (data) => {
            try {
              const res = await updateTeacherProfile(data);
              setProfile(res.data);
              setShowEditProfile(false);
            } catch {
              alert("Failed to update profile");
            }
          }}
        />
      )}

      {showEduModal && (
        <EducationModal
          initialData={editingEducation}
          onClose={() => {
            setShowEduModal(false);
            setEditingEducation(null);
          }}
          onSave={async (data) => {
            try {
              if (editingEducation) {
                const res = await updateTeacherEducation(
                  editingEducation.id,
                  data
                );
                setEducations(
                  educations.map((e) =>
                    e.id === editingEducation.id ? res.data : e
                  )
                );
              } else {
                const res = await addTeacherEducation(data);
                setEducations([...educations, res.data]);
              }
              setShowEduModal(false);
              setEditingEducation(null);
            } catch {
              alert("Failed to save education");
            }
          }}
        />
      )}

      {showExpModal && (
        <ExperienceModal
          initialData={editingExperience}
          onClose={() => {
            setShowExpModal(false);
            setEditingExperience(null);
          }}
          onSave={async (data) => {
            try {
              if (editingExperience) {
                const res = await updateTeacherExperience(
                  editingExperience.id,
                  data
                );
                setExperiences(
                  experiences.map((e) =>
                    e.id === editingExperience.id ? res.data : e
                  )
                );
              } else {
                const res = await addTeacherExperience(data);
                setExperiences([...experiences, res.data]);
              }
              setShowExpModal(false);
              setEditingExperience(null);
            } catch {
              alert("Failed to save experience");
            }
          }}
        />
      )}
    </div>
  );
};

export default TeacherProfile;
