import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AdminTable from "../../components/Admin/AdminTable";
import { getAdminTeachers, deleteAdminTeacher } from "../../api/adminApi";

const AdminTeachers = () => {
  const navigate = useNavigate();

  const [teachers, setTeachers] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [search, setSearch] = useState("");
  const [debouncedSearch, setDebouncedSearch] = useState("");

  /* ===== DEBOUNCE SEARCH ===== */
  useEffect(() => {
    const timer = setTimeout(() => setDebouncedSearch(search), 500);
    return () => clearTimeout(timer);
  }, [search]);

  /* ===== LOAD DATA ===== */
  const load = async () => {
    const res = await getAdminTeachers(page, debouncedSearch);
    setTeachers(res.data.content || []);
    setTotalPages(res.data.totalPages || 0);
  };

  useEffect(() => {
    load();
  }, [page, debouncedSearch]);

  return (
    <>
      <h2>Teachers</h2>

      <div style={{ display: "flex", gap: 12, marginBottom: 16 }}>
        <input
          placeholder="Search teacher..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <AdminTable
        columns={["id", "name", "email"]}
        data={teachers}
        actions={(t) => (
          <>
            <button onClick={() => navigate(`/admin/teachers/${t.id}`)}>
              View
            </button>

            <button onClick={() => navigate(`/admin/teachers/${t.id}/edit`)}>
              Edit
            </button>

            <button
              onClick={async () => {
                if (!confirm("Delete this teacher?")) return;
                await deleteAdminTeacher(t.id);
                load();
              }}
            >
              Delete
            </button>
          </>
        )}
      />

      {/* ===== PAGINATION ===== */}
      <div style={{ marginTop: 16, display: "flex", gap: 8 }}>
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
    </>
  );
};

export default AdminTeachers;
