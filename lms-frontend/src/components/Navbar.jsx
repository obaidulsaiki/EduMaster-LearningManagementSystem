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
  Heart,
  Bell,
  Info,
  Sun,
  Moon,
} from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { useNotifications } from "../hooks/useNotifications";
import { applyTheme } from "../theme";
import { updatePreferences } from "../api/settingsApi";
import "./Navbar.css";

const SCROLL_TRIGGER = 50;

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const { notifications, unreadCount, markAllAsRead } = useNotifications();

  const [isScrolled, setIsScrolled] = useState(false);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const [isNotifOpen, setIsNotifOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [darkMode, setDarkMode] = useState(
    localStorage.getItem("theme") !== "light"
  );

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

  /* ================= THEME TOGGLE ================= */
  const toggleTheme = () => {
    const newDarkMode = !darkMode;
    setDarkMode(newDarkMode);
    const theme = newDarkMode ? "dark" : "light";
    applyTheme(theme);

    // Sync with backend for Students
    if (user?.role === "STUDENT") {
      updatePreferences({ darkMode: newDarkMode }).catch(() => {});
    }
  };

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
        {/* BRANDING */}
        <Link to="/" className="brand-container">
          <div className="brand-icon-box">L</div>
          <div className="brand-text">
            Edu<span className="brand-highlight">Master</span>
          </div>
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
          {/* THEME TOGGLE */}
          <button
            className="theme-toggle-btn"
            onClick={toggleTheme}
            aria-label="Toggle Theme"
          >
            {darkMode ? <Sun size={20} /> : <Moon size={20} />}
          </button>

          {user ? (
            <>
              {/* NOTIFICATIONS */}
              <div className="notification-dropdown-container">
                <button
                  className={`notif-btn ${unreadCount > 0 ? "has-unread" : ""}`}
                  onClick={() => {
                    setIsNotifOpen((v) => !v);
                    setIsProfileOpen(false);
                  }}
                >
                  <Bell size={20} />
                  {unreadCount > 0 && (
                    <span className="notif-badge">{unreadCount}</span>
                  )}
                </button>

                {isNotifOpen && (
                  <div className="notif-dropdown active">
                    <div className="notif-header">
                      <h4>Notifications</h4>
                      {unreadCount > 0 && (
                        <button onClick={markAllAsRead}>Mark all read</button>
                      )}
                    </div>
                    <div className="notif-list">
                      {notifications.length === 0 ? (
                        <div className="notif-empty">No new notifications</div>
                      ) : (
                        notifications.map((n, i) => (
                          <div key={i} className={`notif-item ${!n.isRead ? "unread" : ""}`}>
                            <div className="notif-icon-wrapper">
                              <Info size={16} />
                            </div>
                            <div className="notif-content">
                              <p>{n.message}</p>
                              <span className="notif-time">Just now</span>
                            </div>
                          </div>
                        ))
                      )}
                    </div>
                  </div>
                )}
              </div>

              <div className="profile-dropdown-container">
                <button
                  className="profile-btn"
                  onClick={() => {
                    setIsProfileOpen((v) => !v);
                    setIsNotifOpen(false);
                  }}
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

                  {user.role === "STUDENT" && (
                    <Link to="/wishlist" className="dropdown-item">
                      <Heart size={16} /> My Wishlist
                    </Link>
                  )}

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
