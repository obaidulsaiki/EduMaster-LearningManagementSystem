const API_URL = "http://localhost:8080/api/ai";

export const sendAiMessage = async (message) => {
  const token = localStorage.getItem("token");
  const user = JSON.parse(localStorage.getItem("user") || "{}");

  if (!token) {
    throw new Error("You must be logged in to use AI assistant.");
  }

  const res = await fetch(`${API_URL}/chat`, {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      message: message,
      userId: user.id || user.sId || user.tId || user.adId,
    }),
  });

  if (!res.ok) {
    const errorBody = await res.text();
    throw new Error(errorBody || "AI request failed");
  }

  return await res.json();
};