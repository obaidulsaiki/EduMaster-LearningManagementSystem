// src/api/settingsApi.js
import api from "./api"; // 🔥 MUST be the secured axios instance

export const updatePreferences = (data) => {
  return api.put("/user/preferences", data);
};

export const updateEmail = (data) => {
  return api.put("/user/email", data); // Expects { email, code }
};

export const updatePassword = (data) => {
  return api.put("/user/password", data); // Expects { oldPassword, newPassword, code }
};

export const requestVerification = (type) => {
  return api.post("/user/request-verification", { type });
};

export const deleteAccount = () => {
  return api.delete("/user");
};
