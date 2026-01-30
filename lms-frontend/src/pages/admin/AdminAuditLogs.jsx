import React, { useEffect, useState } from "react";
import { getAdminAuditLogs, downloadAuditLogsCsv } from "../../api/adminApi";
import { ClipboardList, Download, Search } from "lucide-react";
import "./AdminAuditLogs.css";

const AdminAuditLogs = () => {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    fetchLogs();
  }, []);

  const fetchLogs = async () => {
    try {
      const resp = await getAdminAuditLogs();
      setLogs(resp.data);
    } catch (err) {
      console.error("Error fetching logs", err);
    } finally {
      setLoading(false);
    }
  };

  const handleExport = async () => {
    try {
      const resp = await downloadAuditLogsCsv();
      const url = window.URL.createObjectURL(new Blob([resp.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `audit-logs-${new Date().toISOString().split('T')[0]}.csv`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("Export failed", err);
    }
  };

  const filteredLogs = logs.filter(log => 
    log.adminName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    log.action.toLowerCase().includes(searchTerm.toLowerCase()) ||
    log.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="audit-logs-page">
      <div className="audit-header">
        <div className="title-section">
          <div className="icon-box"><ClipboardList size={24} /></div>
          <div>
            <h1>Audit Logs</h1>
            <p>Track every administrative action on the platform</p>
          </div>
        </div>
        <button className="export-btn" onClick={handleExport}>
          <Download size={18} /> Export CSV
        </button>
      </div>

      <div className="logs-controls">
        <div className="search-bar">
          <Search size={20} />
          <input 
            type="text" 
            placeholder="Search logs..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      <div className="logs-container">
        {loading ? (
          <div className="logs-loading">Loading activities...</div>
        ) : (
          <table className="logs-table">
            <thead>
              <tr>
                <th>Timestamp</th>
                <th>Admin</th>
                <th>Action</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {filteredLogs.map((log) => (
                <tr key={log.id}>
                  <td className="time-cell">{new Date(log.timestamp).toLocaleString()}</td>
                  <td className="admin-cell"><span className="admin-badge">{log.adminName}</span></td>
                  <td>
                    <span className={`action-badge ${log.action.toLowerCase()}`}>
                      {log.action.replace(/_/g, " ")}
                    </span>
                  </td>
                  <td className="desc-cell">{log.description}</td>
                </tr>
              ))}
              {filteredLogs.length === 0 && (
                <tr><td colSpan="4" className="no-logs">No matching activities.</td></tr>
              )}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default AdminAuditLogs;
