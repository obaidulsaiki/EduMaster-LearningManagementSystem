import React, { useState, useEffect } from "react";
import { getAdminReport, downloadAdminReport, downloadRevenueCsv } from "../../api/adminApi";
import { 
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
  LineChart, Line, AreaChart, Area, PieChart, Pie, Cell
} from "recharts";
import { 
  TrendingUp, Users, BookOpen, DollarSign, Download, 
  FileText, Calendar, Filter, ChevronRight
} from "lucide-react";
import "./AdminAnalytics.css";

const AdminAnalytics = () => {
  const [month, setMonth] = useState(new Date().toISOString().slice(0, 7)); // YYYY-MM
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, [month]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const resp = await getAdminReport(month);
      setData(resp.data);
    } catch (err) {
      console.error("Error fetching analytics", err);
    } finally {
      setLoading(false);
    }
  };

  const handleDownloadPDF = async () => {
    try {
      const resp = await downloadAdminReport(month);
      const url = window.URL.createObjectURL(new Blob([resp.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `Report-${month}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("PDF Download failed", err);
    }
  };

  const handleDownloadCSV = async () => {
    try {
      const resp = await downloadRevenueCsv(month);
      const url = window.URL.createObjectURL(new Blob([resp.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `Revenue-${month}.csv`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("CSV Download failed", err);
    }
  };

  // Mock trend data for visualization (in reality these would come from backend)
  const trendData = [
    { name: 'Mon', students: 4, revenue: 120 },
    { name: 'Tue', students: 7, revenue: 210 },
    { name: 'Wed', students: 5, revenue: 150 },
    { name: 'Thu', students: 8, revenue: 240 },
    { name: 'Fri', students: 12, revenue: 360 },
    { name: 'Sat', students: 15, revenue: 450 },
    { name: 'Sun', students: 10, revenue: 300 },
  ];

  const categoryData = [
    { name: 'Development', value: 45 },
    { name: 'Design', value: 25 },
    { name: 'Marketing', value: 15 },
    { name: 'Business', value: 15 },
  ];

  const COLORS = ['#38bdf8', '#818cf8', '#fbbf24', '#f472b6'];

  if (loading && !data) return <div className="analytics-loading">Loading Analytics...</div>;

  return (
    <div className="analytics-page">
      <div className="analytics-header">
        <div className="header-left">
          <h1>Advanced Analytics</h1>
          <p>Real-time platform insights and financial performance</p>
        </div>
        
        <div className="header-actions">
          <div className="month-picker">
            <Calendar size={18} />
            <input 
              type="month" 
              value={month} 
              onChange={(e) => setMonth(e.target.value)} 
            />
          </div>
          <button className="btn-secondary" onClick={handleDownloadCSV}>
            <FileText size={18} /> CSV
          </button>
          <button className="btn-primary-gradient" onClick={handleDownloadPDF}>
            <Download size={18} /> Export PDF
          </button>
        </div>
      </div>

      {/* STATS CARDS */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon rev"><DollarSign size={24} /></div>
          <div className="stat-info">
            <label>Total Revenue</label>
            <h3>${data?.revenue || 0}</h3>
            <span className="trend positive">+12.5% vs last month</span>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon stu"><Users size={24} /></div>
          <div className="stat-info">
            <label>New Students</label>
            <h3>{data?.newStudents || 0}</h3>
            <span className="trend positive">+8.2% vs last month</span>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon adm"><TrendingUp size={24} /></div>
          <div className="stat-info">
            <label>Admin Earnings</label>
            <h3>${data?.adminCommission || 0}</h3>
            <span className="trend neutral">On track</span>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon cou"><BookOpen size={24} /></div>
          <div className="stat-info">
            <label>Completed Courses</label>
            <h3>{data?.completedCourses || 0}</h3>
            <span className="trend negative">-2.1% vs last month</span>
          </div>
        </div>
      </div>

      {/* CHARTS SECTION */}
      <div className="charts-grid">
        <div className="chart-container large">
          <div className="chart-header">
            <h4>Revenue & Growth Trend</h4>
            <div className="chart-legend">
              <span className="dot revenue"></span> Revenue
              <span className="dot students"></span> Students
            </div>
          </div>
          <div className="chart-wrapper">
            <ResponsiveContainer width="100%" height={300}>
              <AreaChart data={trendData}>
                <defs>
                  <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#38bdf8" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#38bdf8" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e2e8f0" />
                <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} />
                <YAxis axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 12}} />
                <Tooltip 
                  contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)' }}
                />
                <Area type="monotone" dataKey="revenue" stroke="#38bdf8" strokeWidth={3} fillOpacity={1} fill="url(#colorRevenue)" />
                <Area type="monotone" dataKey="students" stroke="#818cf8" strokeWidth={3} fill="none" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="chart-container">
          <div className="chart-header">
            <h4>Course Distribution</h4>
          </div>
          <div className="chart-wrapper flex-center">
            <ResponsiveContainer width="100%" height={260}>
              <PieChart>
                <Pie
                  data={categoryData}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={80}
                  paddingAngle={5}
                  dataKey="value"
                >
                  {categoryData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
            <div className="pie-legend">
              {categoryData.map((item, i) => (
                <div key={i} className="legend-item">
                  <span className="dot" style={{backgroundColor: COLORS[i]}}></span>
                  <span className="label">{item.name}</span>
                  <span className="val">{item.value}%</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminAnalytics;
