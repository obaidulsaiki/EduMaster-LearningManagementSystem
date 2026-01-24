import React, { useEffect, useState } from "react";
import "./Profile.css";
import { Edit, GraduationCap, CheckCircle, Trash2 } from "lucide-react";
import { Download } from "lucide-react";
import { downloadCertificate } from "../api/certificateApi";
import { downloadBlob } from "../utils/downloadFile";
import EditProfileModal from "../components/profile/EditProfileModal";
import CreateProfileForm from "../components/profile/CreateProfileForm";
import EducationModal from "../components/profile/EducationModal";

import { getStudentProfile, getCompletedCourses } from "../api/profileApi";
import {
  getMyEducations,
  createEducation,
  updateEducation,
  deleteEducation,
} from "../api/educationApi";

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [completedCourses, setCompletedCourses] = useState([]);
  const [educations, setEducations] = useState([]);

  const [loading, setLoading] = useState(true);
  const [showEdit, setShowEdit] = useState(false);

  const [showEduModal, setShowEduModal] = useState(false);
  const [selectedEdu, setSelectedEdu] = useState(null);

  useEffect(() => {
    Promise.all([getStudentProfile(), getCompletedCourses(), getMyEducations()])
      .then(([profileRes, courseRes, eduRes]) => {
        setProfile(profileRes.data);
        setCompletedCourses(courseRes.data || []);

        const eduData = Array.isArray(eduRes.data)
          ? eduRes.data
          : eduRes.data?.educations || [];

        setEducations(eduData);
      })
      .catch((err) => {
        console.error("Profile load failed", err);
        setProfile(null);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="profile-page">Loading...</div>;

  if (!profile) {
    return (
      <div className="profile-page">
        <CreateProfileForm onSuccess={setProfile} />
      </div>
    );
  }

  return (
    <div className="profile-page">
      {/* ===== HEADER ===== */}
      <div className="profile-header">
        <div className="avatar">{profile.name?.charAt(0).toUpperCase()}</div>

        <div className="profile-info">
          <h2>{profile.name}</h2>
          <p className="email">{profile.email}</p>
          <span className="role">{profile.role}</span>
        </div>

        <button className="edit-btn" onClick={() => setShowEdit(true)}>
          <Edit size={16} /> Edit Profile
        </button>
      </div>

      {/* ===== ABOUT ===== */}
      <section className="profile-section">
        <h3>About</h3>
        <p className="bio">
          {profile.bio || "Add a short bio to your profile."}
        </p>
      </section>

      {/* ===== EDUCATION ===== */}
      <section className="profile-section">
        <div className="section-header">
          <h3>Education</h3>
          <button
            onClick={() => {
              setSelectedEdu(null);
              setShowEduModal(true);
            }}
          >
            + Add
          </button>
        </div>

        {educations.length === 0 && (
          <p className="empty-text">No education added yet</p>
        )}

        {educations.map((edu) => (
          <div key={edu.id} className="education-card">
            <GraduationCap />

            <div>
              <h4>{edu.type}</h4>
              <p>{edu.instituteName}</p>
              <span>
                {edu.startYear} – {edu.endYear || "Present"}
              </span>
            </div>

            <div className="edu-actions">
              <Edit
                size={16}
                onClick={() => {
                  setSelectedEdu(edu);
                  setShowEduModal(true);
                }}
              />

              <Trash2
                size={16}
                onClick={async () => {
                  await deleteEducation(edu.id);
                  setEducations(educations.filter((e) => e.id !== edu.id));
                }}
              />
            </div>
          </div>
        ))}
      </section>

      {/* ===== COMPLETED COURSES ===== */}
      {/* ===== COMPLETED COURSES ===== */}
      <section className="profile-section">
        <h3>Completed Courses</h3>

        {completedCourses.length === 0 && (
          <p className="empty-text">No completed courses yet</p>
        )}

        <div className="completed-grid">
          {completedCourses.map((course) => (
            <div key={course.courseId} className="course-card">
              <CheckCircle className="completed-icon" />

              <h4>{course.courseTitle}</h4>

              <div className="progress-bar">
                <div
                  className="progress-fill"
                  style={{ width: `${course.progress}%` }}
                />
              </div>

              <span className="completed-date">
                Completed • {course.completedAt}
              </span>

              {/* 🔥 DOWNLOAD BUTTON */}
              <button
                className="download-btn"
                onClick={async () => {
                  try {
                    const res = await downloadCertificate(course.courseId);
                    downloadBlob(
                      res.data,
                      `${course.courseTitle}-certificate.pdf`,
                    );
                  } catch (err) {
                    alert("Failed to download certificate");
                  }
                }}
              >
                <Download size={16} /> Download Certificate
              </button>
            </div>
          ))}
        </div>
      </section>

      {/* ===== EDIT PROFILE MODAL ===== */}
      {showEdit && (
        <EditProfileModal
          profile={profile}
          onClose={() => setShowEdit(false)}
          onUpdate={(updated) => setProfile(updated)}
        />
      )}

      {/* ===== EDUCATION MODAL ===== */}
      {showEduModal && (
        <EducationModal
          education={selectedEdu}
          onClose={() => setShowEduModal(false)}
          onSave={async (data) => {
            if (selectedEdu) {
              const res = await updateEducation(selectedEdu.id, data);
              setEducations(
                educations.map((e) => (e.id === selectedEdu.id ? res.data : e)),
              );
            } else {
              const res = await createEducation(data);
              setEducations([...educations, res.data]);
            }
            setShowEduModal(false);
          }}
        />
      )}
    </div>
  );
};

export default Profile;
