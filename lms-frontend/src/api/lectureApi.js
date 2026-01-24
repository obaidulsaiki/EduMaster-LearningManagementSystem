import api from "./api";

// mark lecture completed
export const completeLecture = (lectureId) =>
  api.post(`/student/course/lecture/${lectureId}/complete`);
/* ================= LECTURES ================= */
export const getLectureById = (lectureId) =>
  api.get(`/student/lectures/${lectureId}`);
