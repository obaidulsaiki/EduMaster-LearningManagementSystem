import api from "./api";

/* ================= PROFILE ================= */

// Logged-in teacher profile
export const getMyTeacherProfile = () => {
  return api.get("/teacher/profile/me");
};

// Public teacher profile (view other teachers)
export const getTeacherProfile = (teacherId) => {
  return api.get(`/teacher/profile/${teacherId}`);
};

// Create or update teacher profile
export const updateTeacherProfile = (data) => {
  return api.post("/teacher/profile", data);
};

/* ================= DASHBOARD ================= */

export const getTeacherDashboard = () => {
  return api.get("/teacher/dashboard");
};

/* ================= EDUCATION ================= */

// Get my education list
export const getMyTeacherEducations = () => {
  return api.get("/teacher/education/me");
};

// Add education
export const addTeacherEducation = (data) => {
  return api.post("/teacher/education", data);
};

// Update education
export const updateTeacherEducation = (id, data) => {
  return api.put(`/teacher/education/${id}`, data);
};

// Delete education
export const deleteTeacherEducation = (id) => {
  return api.delete(`/teacher/education/${id}`);
};

/* ================= EXPERIENCE ================= */

// Get my experience list
export const getMyTeacherExperiences = () => {
  return api.get("/teacher/experience/me");
};

// Add experience
export const addTeacherExperience = (data) => {
  return api.post("/teacher/experience", data);
};

// Update experience
export const updateTeacherExperience = (id, data) => {
  return api.put(`/teacher/experience/${id}`, data);
};

// Delete experience
export const deleteTeacherExperience = (id) => {
  return api.delete(`/teacher/experience/${id}`);
};
