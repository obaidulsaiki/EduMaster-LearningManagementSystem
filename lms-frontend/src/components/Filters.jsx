import { useEffect, useState } from "react";
import { fetchCategories } from "../services/courseService";
import "./Filters.css"; // Don't forget to import the CSS!

const Filters = ({ filters, setFilters }) => {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      const data = await fetchCategories();
      setCategories(data);
    } catch (err) {
      console.error("Failed to load categories", err);
    }
  };

  return (
    <aside className="filters">
      <h3>Filter Courses</h3>

      {/* CATEGORY */}
      <div>
        <label>Category</label>
        <select
          value={filters.category}
          onChange={(e) => setFilters({ ...filters, category: e.target.value })}
          style={{ marginTop: "12px" }}
        >
          <option value="">All Categories</option>
          {categories.map((cat) => (
            <option key={cat} value={cat}>
              {cat}
            </option>
          ))}
        </select>
      </div>

      {/* PRICE RANGE (Dynamic Grid Layout) */}
      <div>
        <label style={{ marginBottom: "12px", display: "block" }}>
          Price Range
        </label>
        <div className="price-group">
          <input
            type="number"
            placeholder="Min"
            value={filters.minPrice}
            onChange={(e) =>
              setFilters({ ...filters, minPrice: e.target.value })
            }
          />
          <input
            type="number"
            placeholder="Max"
            value={filters.maxPrice}
            onChange={(e) =>
              setFilters({ ...filters, maxPrice: e.target.value })
            }
          />
        </div>
      </div>

      {/* SORT */}
      <div>
        <label>Sort By</label>
        <select
          value={filters.sort}
          onChange={(e) => setFilters({ ...filters, sort: e.target.value })}
          style={{ marginTop: "12px" }}
        >
          <option value="">Recommended</option>
          <option value="price,asc">Price: Low to High</option>
          <option value="price,desc">Price: High to Low</option>
        </select>
      </div>
    </aside>
  );
};

export default Filters;
