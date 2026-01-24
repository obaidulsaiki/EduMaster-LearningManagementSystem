import api from "./api";

/* ================= COURSES ================= */

// ✅ FIXED: matches backend
// GET /api/teacher/courses
export const getMyCourses = () => {
  return api.get("/teacher/courses/my");
};

// Create course
export const createCourse = (data) => {
  return api.post("/teacher/courses", data);
};

// Update course
export const updateCourse = (courseId, data) => {
  return api.put(`/teacher/courses/${courseId}`, data);
};

// Delete course
export const deleteCourse = (courseId) => {
  return api.delete(`/teacher/courses/${courseId}`);
};
export const publishCourse = (courseId) => {
  return api.put(`/teacher/courses/${courseId}/publish`);
};

export const unpublishCourse = (courseId) => {
  return api.put(`/teacher/courses/${courseId}/unpublish`);
};
