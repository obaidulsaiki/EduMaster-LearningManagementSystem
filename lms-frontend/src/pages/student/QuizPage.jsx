import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import quizApi from "../../api/quizApi";
import "./QuizPage.css";

const QuizPage = () => {
    const { courseId } = useParams();
    const navigate = useNavigate();
    const [quiz, setQuiz] = useState(null);
    const [answers, setAnswers] = useState({});
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [result, setResult] = useState(null);
    const [uploading, setUploading] = useState({}); // Tracking uploads per question

    useEffect(() => {
        loadQuiz();
    }, [courseId]);

    const loadQuiz = async () => {
        try {
            const res = await quizApi.getQuizByCourseId(courseId);
            const formatted = {
                ...res.data,
                questions: res.data.questions.map(q => ({
                    ...q,
                    extraData: q.extraData ? JSON.parse(q.extraData) : {}
                }))
            };
            setQuiz(formatted);
        } catch (err) {
            console.error("Failed to load quiz", err);
        } finally {
            setLoading(false);
        }
    };

    const handleAnswerChange = (questionId, value) => {
        if (result) return;
        setAnswers({
            ...answers,
            [questionId]: value
        });
    };

    const handleFileUpload = async (questionId, file) => {
        setUploading({ ...uploading, [questionId]: true });
        try {
            const res = await quizApi.uploadAssignment(questionId, file);
            handleAnswerChange(questionId, res.data.id); // Store submission ID
        } catch (err) {
            console.error("File upload failed", err);
            alert("File upload failed. Please try again.");
        } finally {
            setUploading({ ...uploading, [questionId]: false });
        }
    };

    const handleSubmit = async () => {
        const totalQuestions = quiz.questions.length;
        if (Object.keys(answers).length < totalQuestions) {
            alert("Please provide answers for all parts before submitting.");
            return;
        }

        setSubmitting(true);
        try {
            const submission = {
                courseId: parseInt(courseId),
                answers: Object.entries(answers).map(([qId, val]) => {
                    const q = quiz.questions.find(quest => quest.id === parseInt(qId));
                    return {
                        questionId: parseInt(qId),
                        selectedOptionIndex: q.type === 'MULTIPLE_CHOICE' ? val : null,
                        textResponse: q.type === 'FILL_IN_THE_BLANKS' ? val : null,
                        matchingResponse: q.type === 'MATCHING' ? val : null,
                        assignmentSubmissionId: q.type === 'FILE_SUBMISSION' ? val : null
                    };
                })
            };
            const res = await quizApi.submitQuiz(submission);
            setResult(res.data);
        } catch (err) {
            console.error("Failed to submit quiz", err);
            alert("Submission failed. Please try again.");
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <p className="page-loading">Loading Quiz...</p>;
    if (!quiz || !quiz.questions || quiz.questions.length === 0) return <p className="empty-text">No quiz available for this course yet.</p>;

    if (result) {
        return (
            <div className="quiz-result-container">
                <div className={`result-card ${result.passed ? 'passed' : 'failed'}`}>
                    <h2>{result.status === 'PENDING_REVIEW' ? 'Assignment Submitted' : (result.passed ? 'Congratulations!' : 'Keep Practicing!')}</h2>
                    <div className="score-display">
                        <span className="label">Your Score:</span>
                        <span className="value">{result.score} / {quiz.questions.length}</span>
                    </div>
                    <p className="message">
                        {result.status === 'PENDING_REVIEW' 
                            ? "Your automatic answers are graded, but your files need to be reviewed by the teacher. You'll be notified soon!"
                            : (result.passed 
                                ? "You've successfully passed the quiz and earned your certificate!"
                                : "You need at least 75% correct answers to pass. You can retake the quiz anytime.")}
                    </p>
                    <div className="result-actions">
                        {result.passed && (
                            <button className="btn-cert" onClick={() => navigate(`/course/${courseId}`)}>
                                Go to Course to Download Certificate
                            </button>
                        )}
                        <button className="btn-retry" onClick={() => window.location.reload()}>
                            {result.passed ? 'Retake for Practice' : 'Retry Quiz'}
                        </button>
                        <button className="btn-back" onClick={() => navigate(`/course/${courseId}`)}>
                            Back to Course
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="student-quiz-page">
            <div className="quiz-header">
                <h1>Course Assessment</h1>
                <p>Complete all tasks to finish the course. Auto-graded: MCQ, Matching, Blanks. Teacher-reviewed: Assignments.</p>
            </div>

            <div className="questions-list">
                {quiz.questions.map((q, index) => (
                    <div key={q.id} className="question-item">
                        <h3 className="question-text">
                            <span className="num">{index + 1}.</span> {q.questionText}
                        </h3>

                        {q.type === 'MULTIPLE_CHOICE' && (
                            <div className="options-list">
                                {q.options.map((option, idx) => (
                                    <label key={idx} className={`option-item ${answers[q.id] === idx ? 'selected' : ''}`}>
                                        <input
                                            type="radio"
                                            name={`question-${q.id}`}
                                            checked={answers[q.id] === idx}
                                            onChange={() => handleAnswerChange(q.id, idx)}
                                        />
                                        <span className="option-label">{String.fromCharCode(65 + idx)}.</span>
                                        <span className="option-text">{option}</span>
                                    </label>
                                ))}
                            </div>
                        )}

                        {q.type === 'MATCHING' && (
                            <div className="matching-student-ui">
                                {Object.keys(q.extraData.pairs || {}).map((key, kIdx) => (
                                    <div key={kIdx} className="match-row">
                                        <span className="match-key">{key}</span>
                                        <select 
                                            value={answers[q.id]?.[key] || ""}
                                            onChange={(e) => {
                                                const currentMatch = answers[q.id] || {};
                                                handleAnswerChange(q.id, { ...currentMatch, [key]: e.target.value });
                                            }}
                                        >
                                            <option value="">Select Match...</option>
                                            {/* Shuffle or just list candidates */}
                                            {Object.values(q.extraData.pairs).map((v, vIdx) => (
                                                <option key={vIdx} value={v}>{v}</option>
                                            ))}
                                        </select>
                                    </div>
                                ))}
                            </div>
                        )}

                        {q.type === 'FILL_IN_THE_BLANKS' && (
                            <div className="blanks-student-ui">
                                <input 
                                    type="text" 
                                    placeholder="Your answers (comma separated)..."
                                    value={answers[q.id] || ""}
                                    onChange={(e) => handleAnswerChange(q.id, e.target.value)}
                                    className="blank-input-field"
                                />
                                <small>Enter each answer in order, separated by commas.</small>
                            </div>
                        )}

                        {q.type === 'FILE_SUBMISSION' && (
                            <div className="file-student-ui">
                                {answers[q.id] ? (
                                    <div className="upload-success">
                                        <span>✅ File Uploaded Successfully (ID: {answers[q.id]})</span>
                                        <button onClick={() => handleAnswerChange(q.id, null)} className="btn-mini-remove">Delete</button>
                                    </div>
                                ) : (
                                    <div className="upload-box">
                                        <input 
                                            type="file" 
                                            onChange={(e) => handleFileUpload(q.id, e.target.files[0])}
                                            disabled={uploading[q.id]}
                                        />
                                        {uploading[q.id] && <span className="uploading-text">Uploading...</span>}
                                    </div>
                                )}
                            </div>
                        )}
                    </div>
                ))}
            </div>

            <div className="quiz-footer">
                <div className="progress">
                    Tasks completed: {Object.keys(answers).length} / {quiz.questions.length}
                </div>
                <button 
                    className="btn-submit" 
                    onClick={handleSubmit}
                    disabled={submitting || Object.values(uploading).some(u => u)}
                >
                    {submitting ? 'Submitting...' : 'Finish & Submit Assessment'}
                </button>
            </div>
        </div>
    );
};

export default QuizPage;
