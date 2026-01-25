import api from "./api";

export const fetchMentors = async () => {
    const response = await api.get("/public/teachers");
    return response.data;
};
