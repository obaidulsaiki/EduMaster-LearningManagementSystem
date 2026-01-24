import api from "./api";

export const getAdminProfile = () =>
  api.get("/admin/profile/me");

export const updateAdminProfile = (data) =>
  api.post("/admin/profile", data);
