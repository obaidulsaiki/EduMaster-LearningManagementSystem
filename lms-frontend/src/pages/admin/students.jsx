import { useEffect, useState } from "react";
import AdminLayout from "./AdminLayout";
import AdminTable from "../../components/Admin/AdminTable";
import { getAdminStudents, toggleAdminStudentStatus } from "../../api/adminApi";
import "./AdminStudents.css";
const AdminStudents = () => {
  const [students, setStudents] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [search, setSearch] = useState("");
  const [debouncedSearch, setDebouncedSearch] = useState("");
  const [loading, setLoading] = useState(false);

  /* ===== DEBOUNCE SEARCH ===== */
  useEffect(() => {
    const timer = setTimeout(() => setDebouncedSearch(search), 400);
    return () => clearTimeout(timer);
  }, [search]);

  /* ===== LOAD DATA ===== */
  const load = async () => {
    setLoading(true);
    try {
      const res = await getAdminStudents(page, debouncedSearch);
      setStudents(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [page, debouncedSearch]);

  return (
    <div className="admin-page">
      {/* ===== HEADER ===== */}
      <div className="admin-page-header">
        <h2>Students</h2>

        <input
          className="admin-search"
          placeholder="Search student..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      {/* ===== TABLE ===== */}
      <AdminTable
        columns={["id", "name", "email"]}
        data={students}
        loading={loading}
        actions={(s) => (
          <div className="admin-actions">
            <button
              style={{
                backgroundColor: s.enabled ? "#ef4444" : "#10b981",
                color: "white",
                border: "none",
                borderRadius: "4px",
                padding: "6px 12px"
              }}
              onClick={async () => {
                const action = s.enabled ? "Disable" : "Enable";
                if (!confirm(`${action} this student?`)) return;
                await toggleAdminStudentStatus(s.id);
                load();
              }}
            >
              {s.enabled ? "Disable" : "Enable"}
            </button>
          </div>
        )}
      />

      {/* ===== PAGINATION ===== */}
      {totalPages > 1 && (
        <div className="admin-pagination">
          <button disabled={page === 0} onClick={() => setPage(page - 1)}>
            Prev
          </button>

          <span>
            Page {page + 1} of {totalPages}
          </span>

          <button
            disabled={page + 1 >= totalPages}
            onClick={() => setPage(page + 1)}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default AdminStudents;
