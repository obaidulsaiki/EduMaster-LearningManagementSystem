import React, { useState, useEffect } from "react";
import { Link, NavLink, useNavigate, useLocation } from "react-router-dom";
import {
  Menu,
  X,
  Search,
  ChevronDown,
  LogOut,
  User,
  Settings,
  LayoutDashboard,
} from "lucide-react";
import { useAuth } from "../context/AuthContext";
import "./Navbar.css";

const SCROLL_TRIGGER = 50;

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();

  const [isScrolled, setIsScrolled] = useState(false);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");

  const isHome = location.pathname === "/";

  /* ================= SCROLL EFFECT ================= */
  useEffect(() => {
    const handleScroll = () => {
      if (isHome) {
        setIsScrolled(window.scrollY > SCROLL_TRIGGER);
      } else {
        setIsScrolled(true);
      }
    };

    handleScroll();
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [isHome, location.pathname]);

  /* ================= LOGOUT ================= */
  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  /* ================= NAVBAR STYLE ================= */
  const getNavbarClass = () => {
    if (!isHome) return "navbar";
    return isScrolled ? "navbar scrolled" : "navbar on-hero";
  };

  /* ================= ROLE-AWARE ROUTES ================= */
  const getProfileRoute = () => {
    if (!user) return "/login";

    switch (user.role) {
      case "TEACHER":
        return "/teacher/profile";
      case "ADMIN":
        return "/admin/profile";
      default:
        return "/profile"; // STUDENT
    }
  };

  return (
    <nav className={getNavbarClass()}>
      <div className="navbar-container">
        {/* LOGO */}
        <Link to="/" className="navbar-logo">
          <div className="logo-icon">L</div>
          <span className="logo-text">
            Edu<span className="highlight">Master</span>
          </span>
        </Link>

        {/* SEARCH */}
        <div className="navbar-search">
          <Search size={18} />
          <input
            type="text"
            placeholder="Search for courses..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                navigate(`/browse?search=${encodeURIComponent(searchQuery)}`);
              }
            }}
          />
        </div>

        {/* NAV LINKS */}
        <div className="nav-menu">
          <NavLink to="/browse" className="nav-link">
            Browse
          </NavLink>
          <NavLink to="/paths" className="nav-link">
            Paths
          </NavLink>
          <NavLink to="/mentors" className="nav-link">
            Mentors
          </NavLink>
        </div>

        {/* ACTIONS */}
        <div className="navbar-actions">
          {user ? (
            <>
              {/* 🔕 Notifications disabled for now */}

              <div className="profile-dropdown-container">
                <button
                  className="profile-btn"
                  onClick={() => setIsProfileOpen((v) => !v)}
                >
                  <span className="username">{user.name}</span>
                  <ChevronDown size={16} />
                </button>

                <div
                  className={`dropdown-menu ${isProfileOpen ? "active" : ""}`}
                >
                  {/* PROFILE (ROLE AWARE) */}
                  <Link to={getProfileRoute()} className="dropdown-item">
                    <User size={16} /> Profile
                  </Link>

                  {/* TEACHER DASHBOARD */}
                  {user.role === "TEACHER" && (
                    <Link to="/teacher/dashboard" className="dropdown-item">
                      <LayoutDashboard size={16} /> Teacher Dashboard
                    </Link>
                  )}

                  {/* ADMIN DASHBOARD */}
                  {user.role === "ADMIN" && (
                    <Link to="/admin/dashboard" className="dropdown-item">
                      <LayoutDashboard size={16} /> Admin Dashboard
                    </Link>
                  )}

                  <Link to="/settings" className="dropdown-item">
                    <Settings size={16} /> Settings
                  </Link>

                  <div className="dropdown-divider" />

                  <button
                    onClick={handleLogout}
                    className="dropdown-item logout"
                  >
                    <LogOut size={16} /> Logout
                  </button>
                </div>
              </div>
            </>
          ) : (
            <div className="auth-buttons">
              <Link to="/login" className="btn-text">
                Log in
              </Link>
              <Link to="/register" className="btn-primary">
                Join for Free
              </Link>
            </div>
          )}

          {/* MOBILE MENU TOGGLE */}
          <button
            className="mobile-menu-icon"
            onClick={() => setIsMobileMenuOpen((v) => !v)}
          >
            {isMobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
