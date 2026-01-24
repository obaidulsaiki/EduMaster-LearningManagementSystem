import api from "./api";

/* ================= LECTURES ================= */

// ✅ List lectures of a course
export const getLectures = (courseId) => {
  return api.get(`/teacher/courses/${courseId}/lectures`);
};

// ✅ Add lecture
export const addLecture = (courseId, data) => {
  return api.post(`/teacher/courses/${courseId}/lectures`, data);
};

// ✅ Update lecture
export const updateLecture = (lectureId, data) => {
  return api.put(`/teacher/lectures/${lectureId}`, data);
};

// ✅ Delete lecture
export const deleteLecture = (lectureId) => {
  return api.delete(`/teacher/lectures/${lectureId}`);
};

// ✅ Reorder lectures
export const reorderLectures = (courseId, lectureIds) => {
  return api.put(
    `/teacher/courses/${courseId}/lectures/reorder`,
    { lectureIds }
  );
};