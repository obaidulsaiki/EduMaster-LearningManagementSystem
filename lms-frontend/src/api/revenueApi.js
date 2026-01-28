import api from "./api";

export const revenueApi = {
    getTeacherSummary: () => api.get('/revenue/teacher/summary'),
    getAdminSummary: () => api.get('/revenue/admin/summary'),
    requestWithdrawal: (data) => api.post('/revenue/withdraw', data),
    getMyWithdrawals: () => api.get('/revenue/my-withdrawals'),
    getAllPendingWithdrawals: () => api.get('/revenue/admin/all-pending'),
    completeWithdrawal: (id, txId) => api.post(`/revenue/admin/complete/${id}?txId=${txId}`),
    syncRevenue: () => api.post('/revenue/sync'),
    getAllAdminHistory: () => api.get('/revenue/admin/all-history'),
};

export default revenueApi;
