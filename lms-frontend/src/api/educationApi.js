import api from "./api"; // your secureApi (axios instance)

// GET my educations
export const getMyEducations = () =>
  api.get("/student/education");

// CREATE
export const createEducation = (data) =>
  api.post("/student/education", data);

// UPDATE
export const updateEducation = (id, data) =>
  api.put(`/student/education/${id}`, data);

// DELETE
export const deleteEducation = (id) =>
  api.delete(`/student/education/${id}`);
