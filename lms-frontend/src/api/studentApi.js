import secureApi from "./secureApi";

export const getStudentProfile = () =>
  secureApi.get("/student/profile");

export const getCompletedCourses = () =>
  secureApi.get("/student/courses/completed");
