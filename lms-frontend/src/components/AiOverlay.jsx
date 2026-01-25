import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { sendAiMessage } from "../api/aiApi";
import "./AiOverlay.css";

function AiOverlay({ onClose }) {
  const navigate = useNavigate();
  const [messages, setMessages] = useState([
    {
      role: "bot",
      text: "👋 Hi! I’m your Trained LMS Expert. I can suggest courses and help you navigate the platform.",
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
      const data = await sendAiMessage(input);
      // data: { reply, actionType, actionPayload }

      setMessages((m) => [
        ...m,
        {
          role: "bot",
          text: data.reply,
          action: data.actionType !== "NONE" ? data : null,
        },
      ]);

      // Automatically handle simple navigation if the AI suggests it
      if (data.actionType === "NAVIGATE" && data.actionPayload) {
        // Small delay to let user read the message
        setTimeout(() => {
          navigate(data.actionPayload);
          onClose();
        }, 2000);
      }
    } catch (e) {
      setMessages((m) => [
        ...m,
        { role: "bot", text: `⚠️ Error: ${e.message}` },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleAction = (action) => {
    if (action.actionType === "NAVIGATE") {
      navigate(action.actionPayload);
      onClose();
    } else if (action.actionType === "SUGGEST_COURSE") {
      navigate(`/course-details/${action.actionPayload}`);
      onClose();
    }
  };

  return (
    <div className="ai-overlay">
      <div className="ai-header">
        <h3>LMS AI Helper</h3>
        <button onClick={onClose}>✕</button>
      </div>

      <div className="ai-body">
        {messages.map((m, i) => (
          <div
            key={i}
            className={`ai-message ${m.role === "user" ? "ai-user" : "ai-bot"}`}
          >
            <p>{m.text}</p>
            {m.action && m.action.actionType !== "NONE" && (
              <button 
                className="ai-action-btn"
                onClick={() => handleAction(m.action)}
              >
                {m.action.actionType === "NAVIGATE" ? "Go there now" : "View Suggestion"}
              </button>
            )}
          </div>
        ))}

        {loading && <div className="ai-message ai-bot">Thinking…</div>}
      </div>

      <div className="ai-input">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Suggest a course... Where is my profile?"
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
}

export default AiOverlay;
