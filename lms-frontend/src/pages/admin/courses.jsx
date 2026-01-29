import { useEffect, useState } from "react";
import {
  getAdminCourses,
  toggleAdminCourseStatus,
  publishAdminCourse,
} from "../../api/adminApi";
import { Eye, ToggleLeft, ToggleRight, Upload } from "lucide-react";
import "./AdminCourses.css";

const AdminCourses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");

  const loadCourses = async () => {
    setLoading(true);
    try {
      const res = await getAdminCourses();
      setCourses(res.data.content || []);
    } catch (error) {
      console.error("Failed to load courses:", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadCourses();
  }, []);

  const formatCurrency = (amount) => {
    if (!amount) return "৳0";
    return `৳${Number(amount).toLocaleString("en-BD")}`;
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const formatRating = (rating, count) => {
    if (!rating || count === 0) return "No reviews";
    return `${rating.toFixed(1)} ⭐ (${count})`;
  };

  const filteredCourses = courses.filter((course) =>
    course.title?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return (
      <div className="admin-courses-loading">
        <div className="spinner"></div>
        <p>Loading courses...</p>
      </div>
    );
  }

  return (
    <div className="admin-courses-container">
      <div className="admin-courses-header">
        <h2>Course Management</h2>
        <div className="admin-courses-stats">
          <div className="stat-card">
            <span className="stat-label">Total Courses</span>
            <span className="stat-value">{courses.length}</span>
          </div>
          <div className="stat-card">
            <span className="stat-label">Published</span>
            <span className="stat-value">
              {courses.filter((c) => c.published).length}
            </span>
          </div>
          <div className="stat-card">
            <span className="stat-label">Active</span>
            <span className="stat-value">
              {courses.filter((c) => c.enabled).length}
            </span>
          </div>
        </div>
      </div>

      <div className="admin-courses-controls">
        <input
          type="text"
          placeholder="Search courses by title..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      <div className="courses-table-wrapper">
        <table className="admin-courses-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Course Details</th>
              <th>Teacher</th>
              <th>Category</th>
              <th>Price</th>
              <th>Enrollments</th>
              <th>Revenue</th>
              <th>Lectures</th>
              <th>Rating</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredCourses.length === 0 ? (
              <tr>
                <td colSpan="11" className="empty-state">
                  {searchTerm
                    ? "No courses found matching your search"
                    : "No courses available"}
                </td>
              </tr>
            ) : (
              filteredCourses.map((course) => (
                <tr key={course.courseId} className="course-row">
                  <td className="course-id">#{course.courseId}</td>
                  
                  <td className="course-details">
                    <div className="course-title">{course.title}</div>
                    <div className="course-meta">
                      Created: {formatDate(course.createdAt)}
                    </div>
                  </td>

                  <td className="teacher-info">
                    {course.teacherName || "N/A"}
                  </td>

                  <td className="category">
                    <span className="category-badge">{course.category}</span>
                  </td>

                  <td className="price">{formatCurrency(course.price)}</td>

                  <td className="enrollments">
                    <span className="metric-badge enrollments-badge">
                      {course.enrollmentsCount}
                    </span>
                  </td>

                  <td className="revenue">
                    <span className="revenue-amount">
                      {formatCurrency(course.totalRevenue)}
                    </span>
                  </td>

                  <td className="lectures">
                    <span className="metric-badge lectures-badge">
                      {course.lecturesCount}
                    </span>
                  </td>

                  <td className="rating">
                    {formatRating(course.averageRating, course.reviewsCount)}
                  </td>

                  <td className="status">
                    <div className="status-badges">
                      <span
                        className={`status-badge ${
                          course.published ? "published" : "draft"
                        }`}
                      >
                        {course.published ? "Published" : "Draft"}
                      </span>
                      <span
                        className={`status-badge ${
                          course.enabled ? "enabled" : "disabled"
                        }`}
                      >
                        {course.enabled ? "Active" : "Inactive"}
                      </span>
                    </div>
                  </td>

                  <td className="actions">
                    <div className="action-buttons">
                      {!course.published && (
                        <button
                          className="btn-action btn-publish"
                          onClick={async () => {
                            await publishAdminCourse(course.courseId);
                            loadCourses();
                          }}
                          title="Publish Course"
                        >
                          <Upload size={16} />
                        </button>
                      )}

                      <button
                        className={`btn-action ${
                          course.enabled ? "btn-disable" : "btn-enable"
                        }`}
                        onClick={async () => {
                          const action = course.enabled ? "Disable" : "Enable";
                          if (!confirm(`${action} this course?`)) return;
                          await toggleAdminCourseStatus(course.courseId);
                          loadCourses();
                        }}
                        title={course.enabled ? "Disable Course" : "Enable Course"}
                      >
                        {course.enabled ? (
                          <ToggleRight size={16} />
                        ) : (
                          <ToggleLeft size={16} />
                        )}
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {filteredCourses.length > 0 && (
        <div className="table-footer">
          <p>Showing {filteredCourses.length} of {courses.length} courses</p>
        </div>
      )}
    </div>
  );
};

export default AdminCourses;
