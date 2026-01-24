import { useState } from "react";
import { askAI } from "../api/aiApi";
import "./AiOverlay.css";

function AiOverlay({ onClose }) {
  const [messages, setMessages] = useState([
    {
      role: "bot",
      text: "👋 Hi! I’m your AI assistant. Ask me anything about courses or topics.",
    },
  ]);

  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMsg = { role: "user", text: input };
    setMessages((m) => [...m, userMsg]);
    setInput("");
    setLoading(true);

    try {
      const aiReply = await askAI(input);

      setMessages((m) => [
        ...m,
        {
          role: "bot",
          text: aiReply.replace(input, "").trim(),
        },
      ]);
    } catch (e) {
      setMessages((m) => [
        ...m,
        { role: "bot", text: "⚠️ AI service unavailable." },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-overlay">
      <div className="ai-header">
        <h3>AI Learning Assistant</h3>
        <button onClick={onClose}>✕</button>
      </div>

      <div className="ai-body">
        {messages.map((m, i) => (
          <div
            key={i}
            className={`ai-message ${m.role === "user" ? "ai-user" : "ai-bot"}`}
          >
            {m.text}
          </div>
        ))}

        {loading && <div className="ai-message ai-bot">Thinking…</div>}
      </div>

      <div className="ai-input">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Ask something…"
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
}

export default AiOverlay;
