import api from "./api";

// CREATE or UPDATE profile
export const createStudentProfile = (data) =>
  api.post("/student/profile", data);

export const updateStudentProfile = (data) =>
  api.post("/student/profile", data);

// GET profile
export const getStudentProfile = () =>
  api.get("/student/profile/me");

// GET completed courses
export const getCompletedCourses = () =>
  api.get("/student/course/completed");
