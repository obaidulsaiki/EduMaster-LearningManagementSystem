import React, { useEffect, useState } from "react";
import api from "../../api/api";
import AdminTable from "../../components/Admin/AdminTable";
import { CreditCard, Search, Download } from "lucide-react";
import "./AdminPayments.css";

const AdminPayments = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    fetchPayments();
  }, []);

  const fetchPayments = async () => {
    try {
      const res = await api.get("/admin/payments");
      setPayments(res.data || []);
    } catch (err) {
      console.error("Failed to fetch payments", err);
    } finally {
      setLoading(false);
    }
  };

  const filteredPayments = payments.filter((p) =>
    p.studentName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.transactionId.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.courseTitle.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const columns = [
    { header: "Student", key: "studentName", render: (val, row) => (
      <div>
        <div style={{fontWeight: 600}}>{val}</div>
        <div style={{fontSize: '0.75rem', color: 'var(--text-muted)'}}>{row.studentEmail}</div>
      </div>
    )},
    { header: "Course", key: "courseTitle" },
    { header: "Amount", key: "amount", render: (val) => <span style={{fontWeight: 700}}>${val}</span> },
    { header: "Method", key: "method", render: (val) => (
       <span className={`badge-method ${val.toLowerCase()}`}>{val}</span>
    )},
    { header: "Transaction ID", key: "transactionId", render: (val) => <code style={{fontSize: '0.8rem'}}>{val}</code> },
    { header: "Date", key: "paidAt", render: (val) => new Date(val).toLocaleDateString() },
    { header: "Status", key: "status", render: (val) => (
      <span className="status-badge active">{val}</span>
    )}
  ];

  return (
    <div className="admin-page-container">
      <div className="admin-page-header">
        <div>
          <h1>Payment History</h1>
          <p>Monitor all course enrollments and revenue</p>
        </div>
      </div>

      <div className="admin-actions-bar">
        <div className="search-box">
          <Search size={18} />
          <input
            type="text"
            placeholder="Search by student, course or TXN ID..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      <AdminTable
        columns={columns}
        data={filteredPayments}
        loading={loading}
      />
    </div>
  );
};

export default AdminPayments;
