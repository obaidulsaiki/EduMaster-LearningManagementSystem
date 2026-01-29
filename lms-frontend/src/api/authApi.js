// src/api/authApi.js
import api from "./api";

export const login = (data) => {
    return api.post("/auth/login", data);
};

export const register = (data) => {
    return api.post("/auth/register", data);
};

export const checkEmail = (email) => {
    return api.get(`/auth/check-email?email=${email}`);
};

export const forgotPassword = (email) => {
    return api.post("/auth/forgot-password", { email });
};

export const resetPassword = (data) => {
    return api.post("/auth/reset-password", data);
};
