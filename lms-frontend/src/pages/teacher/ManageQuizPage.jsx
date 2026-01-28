import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import quizApi from "../../api/quizApi";
import "./ManageQuizPage.css";

const ManageQuizPage = () => {
  const { courseId } = useParams();
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState(null);

  const showNotify = (msg, type = "success") => {
    setNotification({ msg, type });
    setTimeout(() => setNotification(null), 3000);
  };

  useEffect(() => {
    loadQuiz();
  }, [courseId]);

  const loadQuiz = async () => {
    try {
      const res = await quizApi.getQuizForTeacher(courseId);
      if (res.data && res.data.questions) {
        setQuestions(res.data.questions);
      } else {
        // Initialize with default questions if none exist
        initializeEmptyQuestions();
      }
    } catch (err) {
      console.error("Failed to load quiz", err);
      initializeEmptyQuestions();
    } finally {
      setLoading(false);
    }
  };

  const initializeEmptyQuestions = () => {
    const emptyQuestions = Array.from({ length: 20 }, (_, i) => ({
      questionText: "",
      options: ["", "", "", ""],
      correctOptionIndex: 0,
    }));
    setQuestions(emptyQuestions);
  };

  const handleQuestionChange = (index, value) => {
    const newQuestions = [...questions];
    newQuestions[index].questionText = value;
    setQuestions(newQuestions);
  };

  const handleOptionChange = (qIndex, oIndex, value) => {
    const newQuestions = [...questions];
    newQuestions[qIndex].options[oIndex] = value;
    setQuestions(newQuestions);
  };

  const handleCorrectOptionChange = (qIndex, value) => {
    const newQuestions = [...questions];
    newQuestions[qIndex].correctOptionIndex = parseInt(value);
    setQuestions(newQuestions);
  };

  const handleSave = async () => {
    // Basic validation
    const incomplete = questions.some(q => !q.questionText || q.options.some(o => !o));
    if (incomplete) {
      showNotify("Please fill in all questions and options", "error");
      return;
    }

    try {
      await quizApi.manageQuiz(courseId, { questions });
      showNotify("Quiz saved successfully!");
    } catch (err) {
      console.error("Failed to save quiz", err);
      showNotify("Failed to save quiz", "error");
    }
  };

  if (loading) return <p className="page-loading">Loading...</p>;

  return (
    <div className="manage-quiz-page">
      {notification && (
        <div className={`notification ${notification.type}`}>
          {notification.msg}
        </div>
      )}

      <div className="header">
        <h2>Manage Course Quiz (20 MCQs)</h2>
        <button className="btn-save" onClick={handleSave}>Save Quiz</button>
      </div>

      <div className="questions-container">
        {questions.map((q, qIndex) => (
          <div key={qIndex} className="question-card">
            <h3>Question {qIndex + 1}</h3>
            <textarea
              placeholder="Enter question text..."
              value={q.questionText}
              onChange={(e) => handleQuestionChange(qIndex, e.target.value)}
            />
            <div className="options-grid">
              {q.options.map((option, oIndex) => (
                <div key={oIndex} className="option-input">
                  <label>Option {oIndex + 1}</label>
                  <input
                    type="text"
                    placeholder={`Option ${oIndex + 1}`}
                    value={option}
                    onChange={(e) => handleOptionChange(qIndex, oIndex, e.target.value)}
                  />
                  <input
                    type="radio"
                    name={`correct-${qIndex}`}
                    checked={q.correctOptionIndex === oIndex}
                    onChange={() => handleCorrectOptionChange(qIndex, oIndex)}
                  />
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>

      <div className="footer-actions">
           <button className="btn-save" onClick={handleSave}>Save Quiz</button>
      </div>
    </div>
  );
};

export default ManageQuizPage;
