import { jwtDecode } from "jwt-decode";

export const getAuthUser = () => {
  const token = localStorage.getItem("token");
  if (!token) return null;

  try {
    const decoded = jwtDecode(token);

    return {
      email: decoded.sub,
      role: decoded.role, // STUDENT / TEACHER / ADMIN
      token,
    };
  } catch (err) {
    console.error("Invalid token");
    localStorage.removeItem("token");
    return null;
  }
};
export const logout = () => {
  localStorage.removeItem("token");
  window.location.href = "/login";
};

export const getToken = () => {
  return localStorage.getItem("token");
};
