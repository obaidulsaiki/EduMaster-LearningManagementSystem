import api from "./api";

export const getCourseDetails = (courseId) =>
  api.get(`/courses/${courseId}`);
