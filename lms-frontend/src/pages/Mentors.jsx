import React, { useState, useEffect } from "react";
import { fetchMentors } from "../api/mentorsApi";
import { Mail, Briefcase, BookOpen } from "lucide-react";
import "./Mentors.css";

const Mentors = () => {
  const [mentors, setMentors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadMentors = async () => {
      try {
        const data = await fetchMentors();
        setMentors(data);
      } catch (err) {
        setError("Failed to load mentors. Please try again later.");
      } finally {
        setLoading(false);
      }
    };
    loadMentors();
  }, []);

  if (loading) return <div className="mentors-loading">Loading mentors...</div>;
  if (error) return <div className="mentors-error">{error}</div>;

  return (
    <div className="mentors-page">
      <div className="mentors-header">
        <h1>Our Expert Mentors</h1>
        <p>Learn from industry experts with years of experience.</p>
      </div>

      <div className="mentors-grid">
        {mentors.map((mentor) => (
          <div key={mentor.id} className="mentor-card">
            <div className="mentor-card-header">
              <h3>{mentor.name}</h3>
              <p className="mentor-field">{mentor.field || "Expert Mentor"}</p>
            </div>
            
            <div className="mentor-card-body">
              <div className="mentor-info-item">
                <Mail size={16} />
                <span>{mentor.email}</span>
              </div>
              
              <div className="mentor-info-item">
                <BookOpen size={16} />
                <span>{mentor.courseCount} Courses taking</span>
              </div>

              <div className="mentor-info-item">
                <Briefcase size={16} />
                <span>{mentor.experienceYears || 0} Years Experience</span>
              </div>

              {mentor.bio && <p className="mentor-bio">{mentor.bio}</p>}

              {mentor.experiences && mentor.experiences.length > 0 && (
                <div className="mentor-experience-list">
                  <h4>Work Experience:</h4>
                  {mentor.experiences.slice(0, 2).map((exp, idx) => (
                    <div key={idx} className="experience-item">
                      <strong>{exp.designation}</strong> at {exp.company}
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Mentors;
