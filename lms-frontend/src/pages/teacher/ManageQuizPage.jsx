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
      if (res.data && res.data.questions && res.data.questions.length > 0) {
        setQuestions(res.data.questions.map(q => ({
          ...q,
          type: q.type || 'MULTIPLE_CHOICE',
          extraData: q.extraData ? JSON.parse(q.extraData) : {}
        })));
      } else {
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
    setQuestions([{
      questionText: "",
      type: "MULTIPLE_CHOICE",
      options: ["", "", "", ""],
      correctOptionIndex: 0,
      extraData: {}
    }]);
  };

  const addQuestion = () => {
    setQuestions([...questions, {
      questionText: "",
      type: "MULTIPLE_CHOICE",
      options: ["", "", "", ""],
      correctOptionIndex: 0,
      extraData: {}
    }]);
  };

  const removeQuestion = (index) => {
    if (questions.length === 1) return;
    const newQuestions = questions.filter((_, i) => i !== index);
    setQuestions(newQuestions);
  };

  const handleTypeChange = (index, type) => {
    const newQuestions = [...questions];
    newQuestions[index].type = type;
    // Set defaults for complex types
    if (type === 'MATCHING') {
      newQuestions[index].extraData = { pairs: { "": "" } };
    } else if (type === 'FILL_IN_THE_BLANKS') {
      newQuestions[index].extraData = { answers: [""] };
    } else {
      newQuestions[index].extraData = {};
    }
    setQuestions(newQuestions);
  };

  const handleQuestionChange = (index, field, value) => {
    const newQuestions = [...questions];
    newQuestions[index][field] = value;
    setQuestions(newQuestions);
  };

  const handleOptionChange = (qIndex, oIndex, value) => {
    const newQuestions = [...questions];
    newQuestions[qIndex].options[oIndex] = value;
    setQuestions(newQuestions);
  };

  const handleExtraDataChange = (qIndex, newExtra) => {
    const newQuestions = [...questions];
    newQuestions[qIndex].extraData = newExtra;
    setQuestions(newQuestions);
  };

  const handleSave = async () => {
    // Basic validation
    const incomplete = questions.some(q => !q.questionText);
    if (incomplete) {
      showNotify("Please enter text for all questions", "error");
      return;
    }

    try {
      const payload = {
        questions: questions.map(q => ({
          ...q,
          extraData: JSON.stringify(q.extraData)
        }))
      };
      await quizApi.manageQuiz(courseId, payload);
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
        <h2>Manage Quiz & Assignments</h2>
        <div className="header-actions">
          <button className="btn-add" onClick={addQuestion}>+ Add Question</button>
          <button className="btn-save" onClick={handleSave}>Save Changes</button>
        </div>
      </div>

      <div className="questions-container">
        {questions.map((q, qIndex) => (
          <div key={qIndex} className="question-card">
            <div className="q-card-header">
              <h3>Question {qIndex + 1}</h3>
              <div className="q-card-controls">
                <select 
                  value={q.type} 
                  onChange={(e) => handleTypeChange(qIndex, e.target.value)}
                  className="type-select"
                >
                  <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                  <option value="MATCHING">Matching</option>
                  <option value="FILL_IN_THE_BLANKS">Fill in Blanks</option>
                  <option value="FILE_SUBMISSION">File Submission</option>
                </select>
                <button className="btn-remove" onClick={() => removeQuestion(qIndex)}>&times;</button>
              </div>
            </div>

            <textarea
              placeholder="Enter question or instructions..."
              value={q.questionText}
              onChange={(e) => handleQuestionChange(qIndex, 'questionText', e.target.value)}
            />

            {q.type === 'MULTIPLE_CHOICE' && (
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
                      onChange={() => handleQuestionChange(qIndex, 'correctOptionIndex', oIndex)}
                    />
                  </div>
                ))}
              </div>
            )}

            {q.type === 'MATCHING' && (
              <div className="matching-config">
                <p className="config-label">Matching Pairs (Key {'->'} Value):</p>
                {Object.entries(q.extraData.pairs || {}).map(([key, val], idx) => (
                  <div key={idx} className="pair-row">
                    <input 
                      type="text" 
                      placeholder="Item A" 
                      value={key} 
                      onChange={(e) => {
                        const newPairs = {...q.extraData.pairs};
                        const value = newPairs[key];
                        delete newPairs[key];
                        newPairs[e.target.value] = value;
                        handleExtraDataChange(qIndex, { pairs: newPairs });
                      }}
                    />
                    <input 
                      type="text" 
                      placeholder="Match 1" 
                      value={val} 
                      onChange={(e) => {
                        const newPairs = {...q.extraData.pairs};
                        newPairs[key] = e.target.value;
                        handleExtraDataChange(qIndex, { pairs: newPairs });
                      }}
                    />
                    <button className="btn-mini-remove" onClick={() => {
                        const newPairs = {...q.extraData.pairs};
                        delete newPairs[key];
                        handleExtraDataChange(qIndex, { pairs: newPairs });
                    }}>&times;</button>
                  </div>
                ))}
                <button className="btn-mini-add" onClick={() => {
                    const newPairs = {...q.extraData.pairs, "": ""};
                    handleExtraDataChange(qIndex, { pairs: newPairs });
                }}>+ Add Pair</button>
              </div>
            )}

            {q.type === 'FILL_IN_THE_BLANKS' && (
              <div className="blanks-config">
                <p className="config-label">Correct Answers (Comma separated for multiple blanks):</p>
                <input 
                  type="text" 
                  placeholder="e.g. Java, Python"
                  value={q.extraData.answers?.join(', ') || ""}
                  onChange={(e) => handleExtraDataChange(qIndex, { answers: e.target.value.split(',').map(s => s.trim()) })}
                  className="full-width-input"
                />
                <small>Use placeholders like [blank] in the question text above.</small>
              </div>
            )}

            {q.type === 'FILE_SUBMISSION' && (
              <div className="file-config">
                <p>Students will be required to upload a file for this part.</p>
                <div className="config-row">
                    <label>Allowed Extensions:</label>
                    <input 
                      type="text" 
                      placeholder="pdf, zip, docx"
                      className="full-width-input"
                      value={q.extraData.allowedExtensions?.join(', ') || ""}
                      onChange={(e) => handleExtraDataChange(qIndex, { ...q.extraData, allowedExtensions: e.target.value.split(',').map(s => s.trim()) })}
                    />
                </div>
              </div>
            )}
          </div>
        ))}
      </div>

      <div className="footer-actions">
           <button className="btn-add" onClick={addQuestion}>+ Add Another Question</button>
           <button className="btn-save" onClick={handleSave}>Finalize & Save Quiz</button>
      </div>
    </div>
  );
};

export default ManageQuizPage;
