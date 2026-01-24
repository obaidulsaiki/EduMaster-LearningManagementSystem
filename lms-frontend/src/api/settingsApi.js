// src/api/settingsApi.js
import api from "./api"; // 🔥 MUST be the secured axios instance

export const updatePreferences = (data) => {
  return api.put("/user/preferences", data);
};

export const updateEmail = (email) => {
  return api.put("/user/email", { email });
};

export const updatePassword = (data) => {
  return api.put("/user/password", data);
};

export const deleteAccount = () => {
  return api.delete("/user/delete");
};
