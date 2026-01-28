import React, { useEffect, useState } from "react";
import revenueApi from "../../api/revenueApi";
import "./AdminRevenuePage.css";

const AdminRevenuePage = () => {
    const [summary, setSummary] = useState({ balance: 0, totalEarned: 0 });
    const [pendingRequests, setPendingRequests] = useState([]);
    const [myWithdrawals, setMyWithdrawals] = useState([]);
    const [payoutHistory, setPayoutHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    
    // For admin's own withdrawal
    const [amount, setAmount] = useState("");
    const [method, setMethod] = useState("BANK");

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [summaryRes, pendingRes, myRes, historyRes] = await Promise.all([
                revenueApi.getAdminSummary(),
                revenueApi.getAllPendingWithdrawals(),
                revenueApi.getMyWithdrawals(),
                revenueApi.getAllAdminHistory()
            ]);
            setSummary(summaryRes.data);
            setPendingRequests(pendingRes.data);
            setMyWithdrawals(myRes.data);
            setPayoutHistory(historyRes.data);
        } catch (err) {
            console.error("Failed to load admin revenue data", err);
        } finally {
            setLoading(false);
        }
    };

    const handleAdminWithdraw = async (e) => {
        e.preventDefault();
        const withdrawAmount = parseFloat(amount);
        if (!withdrawAmount || withdrawAmount <= 0) {
            return alert("Please enter a valid amount greater than zero");
        }
        if (withdrawAmount > summary.balance) {
            return alert("Insufficient balance to withdraw " + withdrawAmount);
        }

        try {
            await revenueApi.requestWithdrawal({ amount: withdrawAmount, method });
            alert("Admin payout request submitted successfully!");
            setAmount("");
            loadData();
        } catch (err) {
            alert(err.response?.data?.message || "Withdrawal failed");
        }
    };

    const handleComplete = async (requestId) => {
        const txId = prompt("Enter Transaction ID (from SSLCommerz or Bank):");
        if (!txId) return;

        try {
            await revenueApi.completeWithdrawal(requestId, txId);
            alert("Payout marked as COMPLETED");
            loadData();
        } catch (err) {
            alert("Failed to update status");
        }
    };

    const handleSync = async () => {
        if (!window.confirm("This will recalculate all teacher and admin balances from historical enrollment data. Proceed?")) return;
        setLoading(true);
        try {
            await revenueApi.syncRevenue();
            alert("Data synchronization successful!");
            loadData();
        } catch (err) {
            alert("Sync failed: " + (err.response?.data || err.message));
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <p className="page-loading">Loading...</p>;

    return (
        <div className="admin-revenue-page">
            <div className="header-with-actions">
                <h1>Admin Financial Hub</h1>
                <button className="btn-sync" onClick={handleSync}>🔄 Sync Revenue Data</button>
            </div>

            <div className="revenue-cards">
                <div className="rev-card admin-share">
                    <span className="label">Admin Profit Balance (15%)</span>
                    <span className="value">${(summary?.balance || 0).toFixed(2)}</span>
                </div>
                <div className="rev-card total-platform">
                    <span className="label">Cumulative Admin Earnings</span>
                    <span className="value">${(summary?.totalEarned || 0).toFixed(2)}</span>
                </div>
            </div>

            <div className="admin-sections">
                <div className="section">
                    <h2>Pending Teacher Payouts</h2>
                    <div className="table-wrapper">
                        <table className="revenue-table">
                            <thead>
                                <tr>
                                    <th>User ID</th>
                                    <th>Role</th>
                                    <th>Amount</th>
                                    <th>Method</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {pendingRequests.length === 0 ? (
                                    <tr><td colSpan="5" className="empty-row">No pending payouts</td></tr>
                                ) : (
                                    pendingRequests.map(r => (
                                        <tr key={r.id}>
                                            <td>{r.userId}</td>
                                            <td>{r.userRole}</td>
                                            <td>${(r.amount || 0).toFixed(2)}</td>
                                            <td>{r.paymentMethod}</td>
                                            <td>
                                                <button className="btn-complete" onClick={() => handleComplete(r.id)}>Complete</button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="section admin-withdraw">
                    <h2>Avail Admin Income</h2>
                    <form onSubmit={handleAdminWithdraw} className="withdraw-form">
                         <input 
                            type="number" 
                            placeholder="Amount" 
                            value={amount} 
                            onChange={(e) => setAmount(e.target.value)} 
                         />
                         <select value={method} onChange={(e) => setMethod(e.target.value)}>
                            <option value="BANK">Bank</option>
                            <option value="SSL_PAYOUT">SSLCommerz Payout</option>
                         </select>
                         <button type="submit">Transfer to Bank</button>
                    </form>
                </div>
            </div>

            <div className="section history-full">
                <h2>Financial Payout History</h2>
                <div className="table-wrapper">
                    <table className="revenue-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>User ID</th>
                                <th>Role</th>
                                <th>Amount</th>
                                <th>Method</th>
                                <th>Transaction ID</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {payoutHistory.length === 0 ? (
                                <tr><td colSpan="7" className="empty-row">No payout history found</td></tr>
                            ) : (
                                payoutHistory.map(h => (
                                    <tr key={h.id}>
                                        <td>{new Date(h.requestedAt).toLocaleDateString()}</td>
                                        <td>{h.userId}</td>
                                        <td>{h.userRole}</td>
                                        <td>${(h.amount || 0).toFixed(2)}</td>
                                        <td>{h.paymentMethod}</td>
                                        <td><code>{h.transactionId || 'N/A'}</code></td>
                                        <td><span className={`status ${h.status.toLowerCase()}`}>{h.status}</span></td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default AdminRevenuePage;
