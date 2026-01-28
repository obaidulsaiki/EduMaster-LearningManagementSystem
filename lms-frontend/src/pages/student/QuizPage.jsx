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

    useEffect(() => {
        loadQuiz();
    }, [courseId]);

    const loadQuiz = async () => {
        try {
            const res = await quizApi.getQuizByCourseId(courseId);
            setQuiz(res.data);
        } catch (err) {
            console.error("Failed to load quiz", err);
        } finally {
            setLoading(false);
        }
    };

    const handleOptionSelect = (questionId, optionIndex) => {
        if (result) return; // Prevent change after submission
        setAnswers({
            ...answers,
            [questionId]: optionIndex
        });
    };

    const handleSubmit = async () => {
        const totalQuestions = quiz.questions.length;
        if (Object.keys(answers).length < totalQuestions) {
            alert("Please answer all questions before submitting.");
            return;
        }

        setSubmitting(true);
        try {
            const submission = {
                courseId: parseInt(courseId),
                answers: Object.entries(answers).map(([qId, idx]) => ({
                    questionId: parseInt(qId),
                    selectedOptionIndex: idx
                }))
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
                    <h2>{result.passed ? 'Congratulations!' : 'Keep Practicing!'}</h2>
                    <div className="score-display">
                        <span className="label">Your Score:</span>
                        <span className="value">{result.score} / 20</span>
                    </div>
                    <p className="message">
                        {result.passed 
                            ? "You've successfully passed the quiz and earned your certificate!"
                            : "You need at least 15 correct answers to pass. You can retake the quiz anytime."}
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
                <h1>Course Quiz</h1>
                <p>Answer all 20 questions correctly to earn your certificate. Passing score: 15/20.</p>
            </div>

            <div className="questions-list">
                {quiz.questions.map((q, index) => (
                    <div key={q.id} className="question-item">
                        <h3 className="question-text">
                            <span className="num">{index + 1}.</span> {q.questionText}
                        </h3>
                        <div className="options-list">
                            {q.options.map((option, idx) => (
                                <label key={idx} className={`option-item ${answers[q.id] === idx ? 'selected' : ''}`}>
                                    <input
                                        type="radio"
                                        name={`question-${q.id}`}
                                        checked={answers[q.id] === idx}
                                        onChange={() => handleOptionSelect(q.id, idx)}
                                    />
                                    <span className="option-label">{String.fromCharCode(65 + idx)}.</span>
                                    <span className="option-text">{option}</span>
                                </label>
                            ))}
                        </div>
                    </div>
                ))}
            </div>

            <div className="quiz-footer">
                <div className="progress">
                    Questions answered: {Object.keys(answers).length} / {quiz.questions.length}
                </div>
                <button 
                    className="btn-submit" 
                    onClick={handleSubmit}
                    disabled={submitting}
                >
                    {submitting ? 'Submitting...' : 'Submit Quiz'}
                </button>
            </div>
        </div>
    );
};

export default QuizPage;
