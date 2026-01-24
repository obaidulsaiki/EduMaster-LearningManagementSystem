export const askAI = async (message) => {
  // 1. Change the URL to Groq's endpoint
  const res = await fetch("https://api.groq.com/openai/v1/chat/completions", {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${import.meta.env.VITE_GROQ_API_KEY}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      // 2. Use a Groq-supported model (like Llama 3 or Mixtral)
      model: "llama3-8b-8192", 
      messages: [
        {
          role: "system",
          content: "You are an AI learning assistant...",
        },
        { role: "user", content: message },
      ],
      temperature: 0.6,
      max_tokens: 250,
    }),
  });

  if (!res.ok) {
    throw new Error("AI request failed");
  }

  const data = await res.json();
  return data.choices[0].message.content;
};