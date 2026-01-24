import api from "./api";
export const getCourseProgress = (courseId) =>
  api.get(`/student/course/progress/${courseId}`);
export const getResumeCourse = (courseId) =>
  api.get(`/student/course/${courseId}/resume`);
export const completeLecture = (lectureId) =>
  api.post(`/student/course/lecture/${lectureId}/complete`);