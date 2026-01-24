import { useState } from "react";
import { getAdminReport } from "../../api/adminApi";
import "./AdminReports.css";

const AdminReports = () => {
  const [month, setMonth] = useState("");
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(false);

  const load = async () => {
    if (!month) {
      alert("Please select a month");
      return;
    }

    try {
      setLoading(true);
      const res = await getAdminReport(month);
      setReport(res.data);
    } catch {
      alert("Failed to load report");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="admin-reports">
      <h2>Monthly Report</h2>

      {/* ===== FILTER ===== */}
      <div className="report-controls">
        <input
          type="month"
          value={month}
          onChange={(e) => setMonth(e.target.value)}
        />
        <button onClick={load} disabled={loading}>
          {loading ? "Generating..." : "Generate"}
        </button>
      </div>

      {/* ===== REPORT ===== */}
      {report && (
        <div className="report-grid">
          <div className="report-card">
            <h4>Total Revenue</h4>
            <p>${report.totalRevenue}</p>
          </div>

          <div className="report-card">
            <h4>New Enrollments</h4>
            <p>{report.totalEnrollments}</p>
          </div>

          <div className="report-card">
            <h4>Completed Courses</h4>
            <p>{report.completedCourses}</p>
          </div>

          <div className="report-card">
            <h4>Active Students</h4>
            <p>{report.activeStudents}</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminReports;
