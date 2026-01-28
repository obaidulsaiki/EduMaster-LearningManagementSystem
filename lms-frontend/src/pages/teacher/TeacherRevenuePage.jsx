import React, { useEffect, useState } from "react";
import revenueApi from "../../api/revenueApi";
import "./TeacherRevenuePage.css";

const TeacherRevenuePage = () => {
    const [summary, setSummary] = useState({ balance: 0, totalEarned: 0 });
    const [withdrawals, setWithdrawals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [amount, setAmount] = useState("");
    const [method, setMethod] = useState("BKASH");
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [summaryRes, withdrawalsRes] = await Promise.all([
                revenueApi.getTeacherSummary(),
                revenueApi.getMyWithdrawals()
            ]);
            setSummary(summaryRes.data);
            setWithdrawals(withdrawalsRes.data);
        } catch (err) {
            console.error("Failed to load revenue data", err);
        } finally {
            setLoading(false);
        }
    };

    const handleWithdraw = async (e) => {
        e.preventDefault();
        const withdrawAmount = parseFloat(amount);
        if (!withdrawAmount || withdrawAmount <= 0) {
            return alert("Please enter a valid amount greater than zero");
        }
        if (withdrawAmount > summary.balance) {
            return alert("Insufficient balance to withdraw " + withdrawAmount);
        }

        setSubmitting(true);
        try {
            await revenueApi.requestWithdrawal({ amount: withdrawAmount, method });
            alert("Withdrawal request submitted successfully!");
            setAmount("");
            loadData();
        } catch (err) {
            alert(err.response?.data?.message || "Withdrawal failed");
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <p className="page-loading">Loading revenue data...</p>;

    return (
        <div className="teacher-revenue-page">
            <h1>Revenue & Payouts</h1>

            <div className="revenue-cards">
                <div className="rev-card">
                    <span className="label">Available Balance</span>
                    <span className="value">${(summary?.balance || 0).toFixed(2)}</span>
                </div>
                <div className="rev-card">
                    <span className="label">Total Earned (Lifetime)</span>
                    <span className="value">${(summary?.totalEarned || 0).toFixed(2)}</span>
                </div>
            </div>

            <div className="revenue-content">
                <div className="withdraw-section">
                    <h2>Request Withdrawal</h2>
                    <form onSubmit={handleWithdraw} className="withdraw-form">
                        <div className="form-group">
                            <label>Amount ($)</label>
                            <input 
                                type="number" 
                                value={amount} 
                                onChange={(e) => setAmount(e.target.value)} 
                                placeholder="Min $10"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Method</label>
                            <select value={method} onChange={(e) => setMethod(e.target.value)}>
                                <option value="BKASH">bKash</option>
                                <option value="NAGAD">Nagad</option>
                                <option value="BANK">Bank Transfer</option>
                            </select>
                        </div>
                        <button type="submit" disabled={submitting}>
                            {submitting ? "Submitting..." : "Submit Request"}
                        </button>
                    </form>
                </div>

                <div className="history-section">
                    <h2>Withdrawal History</h2>
                    <div className="table-wrapper">
                        <table className="withdraw-table">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Amount</th>
                                    <th>Method</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {withdrawals.length === 0 ? (
                                    <tr><td colSpan="4" className="empty-row">No withdrawal history</td></tr>
                                ) : (
                                    withdrawals.map(w => (
                                        <tr key={w.id}>
                                            <td>{new Date(w.requestedAt).toLocaleDateString()}</td>
                                            <td>${(w.amount || 0).toFixed(2)}</td>
                                            <td>{w.paymentMethod}</td>
                                            <td><span className={`status ${w.status.toLowerCase()}`}>{w.status}</span></td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TeacherRevenuePage;
