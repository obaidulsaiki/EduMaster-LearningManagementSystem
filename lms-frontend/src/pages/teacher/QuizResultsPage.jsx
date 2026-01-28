import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import quizApi from "../../api/quizApi";
import "./QuizResultsPage.css";

const QuizResultsPage = () => {
    const { courseId } = useParams();
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadResults();
    }, [courseId]);

    const loadResults = async () => {
        try {
            const res = await quizApi.getResults(courseId);
            setResults(res.data || []);
        } catch (err) {
            console.error("Failed to load results", err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <p className="page-loading">Loading...</p>;

    return (
        <div className="quiz-results-page">
            <div className="header">
                <h2>Quiz Results</h2>
                <div className="stats">
                    Total Submissions: {results.length}
                </div>
            </div>

            <div className="table-container">
                <table className="results-table">
                    <thead>
                        <tr>
                            <th>Student Name</th>
                            <th>Score (out of 20)</th>
                            <th>Status</th>
                            <th>Completed At</th>
                        </tr>
                    </thead>
                    <tbody>
                        {results.length === 0 ? (
                            <tr>
                                <td colSpan="4" className="empty-cell">No student has taken the quiz yet.</td>
                            </tr>
                        ) : (
                            results.map((res, index) => (
                                <tr key={index}>
                                    <td>{res.studentName}</td>
                                    <td className="score">{res.score} / 20</td>
                                    <td>
                                        <span className={`status-badge ${res.passed ? 'passed' : 'failed'}`}>
                                            {res.passed ? 'PASSED' : 'FAILED'}
                                        </span>
                                    </td>
                                    <td>{new Date(res.completedAt).toLocaleString()}</td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default QuizResultsPage;
