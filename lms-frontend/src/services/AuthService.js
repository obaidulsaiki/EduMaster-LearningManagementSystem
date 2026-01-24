import axios from "axios";

const API_URL = "http://localhost:8080/api/auth";

export const loginUser  = async (payload) => {
  const response = await axios.post(`${API_URL}/login`, payload);
  return response.data;
};

export const registerUser = async (payload) => {
  const response = await axios.post(`${API_URL}/register`, payload);
  return response.data;
};
export const checkEmailExists = async (email) => {
  const response = await axios.get(`${API_URL}/check-email`, {
    params: { email },
  });
  return response.data; // true / false
};
