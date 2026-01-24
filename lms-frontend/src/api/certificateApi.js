import api from "./api"; // your secured axios instance

export const downloadCertificate = (courseId) =>
  api.get(`/student/certificates/${courseId}/download`, {
    responseType: "blob", // IMPORTANT for PDF
  });
