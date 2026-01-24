import { useEffect, useState } from "react";
import CourseCard from "../components/CourseCard";
import Filters from "../components/Filters";
import { fetchCourses } from "../services/courseService";
import "./BrowseCourses.css";

function BrowseCourses() {
  const [courses, setCourses] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [filters, setFilters] = useState({
    category: "",
    minPrice: "",
    maxPrice: "",
    sort: "",
  });

  useEffect(() => {
    loadCourses();
  }, [page, filters]);

  const loadCourses = async () => {
    try {
      const data = await fetchCourses({
        page,
        size: 6,
        category: filters.category || null,
        minPrice: filters.minPrice || null,
        maxPrice: filters.maxPrice || null,
        sort: filters.sort || null,
      });

      setCourses(data.courses);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error("Failed to load courses", error);
    }
  };

  return (
    <div className="browse-layout">
      <Filters filters={filters} setFilters={setFilters} />

      <div className="course-section">
        <div className="course-grid">
          {courses.map((course) => (
            <CourseCard key={course.courseId} course={course} />
          ))}
        </div>

        {/* PAGINATION */}
        <div className="pagination">
          <button disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
            Prev
          </button>

          {[...Array(totalPages)].map((_, i) => (
            <button
              key={i}
              className={page === i ? "active" : ""}
              onClick={() => setPage(i)}
            >
              {i + 1}
            </button>
          ))}

          <button
            disabled={page === totalPages - 1}
            onClick={() => setPage((p) => p + 1)}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
}

export default BrowseCourses;
