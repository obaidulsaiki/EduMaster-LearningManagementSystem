import { useEffect, useState } from "react";
import { getAdminDashboard } from "../../api/adminApi";

const AdminDashboard = () => {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    getAdminDashboard().then((res) => setStats(res.data));
  }, []);

  if (!stats) return <p>Loading...</p>;

  
  return (
    <div>
      <h1>Admin Analytics</h1>

      <div className="admin-stats">
        <Stat label="Teachers" value={stats.totalTeachers} />
        <Stat label="Students" value={stats.totalStudents} />
        <Stat label="Courses" value={stats.totalCourses} />
        <Stat label="Revenue" value={`$${stats.totalRevenue}`} />
      </div>
    </div>
  );
};

const Stat = ({ label, value }) => (
  <div className="stat-card">
    <p>{label}</p>
    <h2>{value}</h2>
  </div>
);

export default AdminDashboard;
