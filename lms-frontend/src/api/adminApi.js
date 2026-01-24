import api from "./api";

/* ===== DASHBOARD ===== */
export const getAdminDashboard = () =>
  api.get("/admin/dashboard");

/* ===== TEACHERS ===== */
export const getAdminTeachers = (page = 0, search = "") =>
  api.get("/admin/teachers", { params: { page, search } });

export const getAdminTeacherById = (id) =>
  api.get(`/admin/teachers/${id}`);

export const updateAdminTeacher = (id, data) =>
  api.put(`/admin/teachers/${id}`, data);

export const deleteAdminTeacher = (id) =>
  api.delete(`/admin/teachers/${id}`);

/* ===== STUDENTS ===== */
export const getAdminStudents = (page = 0, search = "") =>
  api.get("/admin/students", { params: { page, search } });

export const deleteAdminStudent = (id) =>
  api.delete(`/admin/students/${id}`);

/* ===== COURSES ===== */
export const getAdminCourses = (page = 0, search = "") =>
  api.get("/admin/courses", { params: { page, search } });

export const deleteAdminCourse = (id) =>
  api.delete(`/admin/courses/${id}`);

export const publishAdminCourse = (id) =>
  api.put(`/admin/courses/${id}/publish`);

/* ===== REPORTS ===== */
export const getAdminReport = (month) =>
  api.get("/admin/reports", { params: { month } });
