import { useEffect, useState } from "react";
import AdminTable from "../../components/Admin/AdminTable";
import {
  getAdminCourses,
  deleteAdminCourse,
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
              className="btn-delete"
              onClick={async () => {
                if (!confirm("Delete this course?")) return;
                await deleteAdminCourse(c.courseId);
                loadCourses();
              }}
            >
              Delete
            </button>
          </div>
        )}
      />
    </>
  );
};

export default AdminCourses;
