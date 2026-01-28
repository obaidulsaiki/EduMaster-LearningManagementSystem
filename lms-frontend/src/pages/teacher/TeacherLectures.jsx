import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import {
  getLectures,
  addLecture,
  updateLecture,
  deleteLecture,
  reorderLectures,
} from "../../api/teacherLectureApi";

import LectureModal from "../../components/teacher/LectureModal";
import "./TeacherLectures.css";

const TeacherLectures = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();

  const [lectures, setLectures] = useState([]);
  const [loading, setLoading] = useState(true);

  const [showModal, setShowModal] = useState(false);
  const [editingLecture, setEditingLecture] = useState(null);
  
  const [notification, setNotification] = useState(null);

  /* ================= NOTIFY ================= */

  const showNotify = (msg, type = "success") => {
    setNotification({ msg, type });
    setTimeout(() => setNotification(null), 3000);
  };

  /* ================= LOAD ================= */

  useEffect(() => {
    loadLectures();
  }, [courseId]);

  const loadLectures = async () => {
    try {
      const res = await getLectures(courseId);
      setLectures(res.data || []);
    } catch (err) {
      console.error("Failed to load lectures", err);
    } finally {
      setLoading(false);
    }
  };

  /* ================= REORDER ================= */

  const moveLecture = async (index, direction) => {
    const newList = [...lectures];
    const target = index + direction;

    if (target < 0 || target >= newList.length) return;

    [newList[index], newList[target]] = [newList[target], newList[index]];

    setLectures(newList);

    try {
      await reorderLectures(
        courseId,
        newList.map((l) => l.id),
      );
      showNotify("Order updated");
    } catch {
      showNotify("Failed to reorder", "error");
      loadLectures(); // rollback
    }
  };

  if (loading) return <p className="page-loading">Loading...</p>;

  return (
    <div className="teacher-lectures-page">
      {notification && (
        <div className={`notification ${notification.type}`}>
          {notification.msg}
        </div>
      )}

      <div className="header">
        <h2>Lectures</h2>
        <div className="header-actions">
           <button
            className="btn-secondary"
            onClick={() => navigate(`/teacher/courses/${courseId}/quiz-results`)}
          >
            Quiz Results
          </button>
          <button
            className="btn-secondary"
            onClick={() => navigate(`/teacher/courses/${courseId}/quiz`)}
          >
            Manage Quiz
          </button>
          <button
            className="btn-primary"
            onClick={() => {
              setEditingLecture(null);
              setShowModal(true);
            }}
          >
            + Add Lecture
          </button>
        </div>
      </div>

      {lectures.length === 0 ? (
        <p className="empty-text">No lectures added yet</p>
      ) : (
        <div className="lecture-list">
          {lectures.map((lec, index) => (
            <div key={lec.id} className="lecture-card">
              <span className="order">{index + 1}</span>

              <div className="lecture-info">
                <h4>{lec.title}</h4>
                <p className="video-url">{lec.videoUrl}</p>
              </div>

              <div className="lecture-actions">
                <button onClick={() => moveLecture(index, -1)}>↑</button>
                <button onClick={() => moveLecture(index, 1)}>↓</button>

                <button
                  onClick={() => {
                    setEditingLecture(lec);
                    setShowModal(true);
                  }}
                >
                  Edit
                </button>

                <button
                  className="delete-btn"
                  onClick={async () => {
                    if (!window.confirm("Delete this lecture?")) return;

                    try {
                      await deleteLecture(lec.id);
                      setLectures(lectures.filter((l) => l.id !== lec.id));
                      showNotify("Delete successfully");
                    } catch {
                      showNotify("Failed to delete", "error");
                    }
                  }}
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* ================= MODAL ================= */}
      {showModal && (
        <LectureModal
          initialData={editingLecture}
          onClose={() => {
            setShowModal(false);
            setEditingLecture(null);
          }}
          onSave={async (data) => {
            try {
              if (editingLecture) {
                const res = await updateLecture(
                  editingLecture.id,
                  data,
                );

                setLectures(
                  lectures.map((l) =>
                    l.id === editingLecture.id ? res.data : l,
                  ),
                );
                showNotify("Save successfully");
              } else {
                const res = await addLecture(courseId, data);
                setLectures([...lectures, res.data]);
                showNotify("Lecture added successfully");
              }

              setShowModal(false);
              setEditingLecture(null);
            } catch {
              showNotify("Failed to save", "error");
            }
          }}
        />
      )}
    </div>
  );
};

export default TeacherLectures;
