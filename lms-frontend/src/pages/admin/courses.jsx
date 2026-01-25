import { useEffect, useState } from "react";
import AdminTable from "../../components/Admin/AdminTable";
import {
  getAdminCourses,
  toggleAdminCourseStatus,
  publishAdminCourse,
} from "../../api/adminApi";

const AdminCourses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadCourses = async () => {
    setLoading(true);
    const res = await getAdminCourses();
    setCourses(res.data.content || []);
    setLoading(false);
  };

  useEffect(() => {
    loadCourses();
  }, []);

  if (loading) return <p>Loading courses...</p>;

  return (
    <>
      <h2>Courses</h2>

      <AdminTable
        columns={["courseId", "title", "published"]}
        data={courses}
        actions={(c) => (
          <div className="admin-actions">
            {!c.published && (
              <button
                className="btn-publish"
                onClick={async () => {
                  await publishAdminCourse(c.courseId);
                  loadCourses();
                }}
              >
                Publish
              </button>
            )}

            <button
              style={{
                backgroundColor: c.enabled ? "#ef4444" : "#10b981",
                color: "white",
                border: "none",
                borderRadius: "4px",
                padding: "6px 12px",
                cursor: "pointer"
              }}
              onClick={async () => {
                const action = c.enabled ? "Disable" : "Enable";
                if (!confirm(`${action} this course?`)) return;
                await toggleAdminCourseStatus(c.id);
                loadCourses();
              }}
            >
              {c.enabled ? "Disable" : "Enable"}
            </button>
          </div>
        )}
      />
    </>
  );
};

export default AdminCourses;
