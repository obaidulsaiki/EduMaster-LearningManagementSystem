import axios from "axios";

/* ----------------------------------
   AXIOS INSTANCE
---------------------------------- */
const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

/* ----------------------------------
   COURSES
---------------------------------- */

/**
 * Fetch paginated & filtered courses (PUBLIC)
 * maps to: GET /api/courses/filter
 */
export const fetchCourses = async ({
  page = 0,
  size = 6,
  category = null,
  minPrice = null,
  maxPrice = null,
  sort = null,
}) => {
  try {
    const response = await api.get("/courses/filter", {
      params: {
        page,
        size,
        category: category || null,
        minPrice: minPrice !== "" ? minPrice : null,
        maxPrice: maxPrice !== "" ? maxPrice : null,
        sort: sort || null,
      },
    });

    return response.data; // CoursePageResponseDTO
  } catch (error) {
    console.error("Error fetching courses:", error);
    throw error;
  }
};

/**
 * Fetch course details with lectures + teacher
 * maps to: GET /api/courses/{courseId}
 */
export const getCourseDetails = async (courseId) => {
  try {
    const response = await api.get(`/courses/${courseId}`);
    return response;
  } catch (error) {
    console.error("Error fetching course details:", error);
    throw error;
  }
};

/* ----------------------------------
   CATEGORIES
---------------------------------- */

/**
 * Fetch distinct course categories
 * maps to: GET /api/courses/categories
 */
export const fetchCategories = async () => {
  try {
    const response = await api.get("/courses/categories");
    return response.data; // List<String>
  } catch (error) {
    console.error("Error fetching categories:", error);
    throw error;
  }
};
