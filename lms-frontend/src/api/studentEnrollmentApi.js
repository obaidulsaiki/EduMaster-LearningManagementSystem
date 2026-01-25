import api from "./api";

// Check enrollment status
export const getEnrollmentStatus = (courseId) =>
  api.get(`/student/enrollments/${courseId}/status`);

// Create enrollment (before payment)
export const enrollCourse = (courseId) =>
  api.post(`/student/enrollments/${courseId}`);

// Confirm payment
export const confirmPayment = (courseId, paymentData) =>
  api.post(`/student/enrollments/${courseId}/confirm`, paymentData);
