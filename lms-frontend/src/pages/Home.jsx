import { useState } from "react";
import { Link } from "react-router-dom";
import { Search, Sparkles } from "lucide-react";
import AiOverlay from "../components/AiOverlay";
import "./Home.css";

function Home() {
  const [isAiOpen, setIsAiOpen] = useState(false);

  return (
    <div className="home-container">
      {/* HERO SECTION */}
      <section className="hero">
        <div className="hero-content">
          <h1>
            Learn Smarter with <span className="highlight">AI-Powered</span>{" "}
            Education
          </h1>

          <p>
            Discover personalized learning paths, smart recommendations, and an
            AI assistant that guides you through every course.
          </p>

          <div className="hero-actions">
            <Link to="/browse" className="btn-primary">
              <Search size={20} />
              <span>Browse Courses</span>
            </Link>

            <button className="btn-ai" onClick={() => setIsAiOpen(true)}>
              <Sparkles size={20} />
              <span>Ask AI</span>
            </button>
          </div>
        </div>
      </section>

      {/* AI OVERLAY */}
      {isAiOpen && <AiOverlay onClose={() => setIsAiOpen(false)} />}
    </div>
  );
}

export default Home;
