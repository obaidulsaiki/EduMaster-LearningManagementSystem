import { NavLink, useNavigate } from "react-router-dom";
import {
  BarChart3,
  Users,
  GraduationCap,
  BookOpen,
  FileText,
  UserCog,
  Settings,
  LogOut,
  CreditCard,
  ClipboardList,
} from "lucide-react";
import "./AdminLayout.css";

const AdminSidebar = () => {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <aside className="admin-sidebar">
      {/* ===== BRAND ===== */}
      <div className="admin-brand">
        <h2>Admin</h2>
      </div>

      {/* ===== NAV ===== */}
      <nav className="admin-nav">
        <NavItem to="/admin" icon={<BarChart3 size={18} />} label="Analytics" />
        <NavItem
          to="/admin/teachers"
          icon={<Users size={18} />}
          label="Teachers"
        />
        <NavItem
          to="/admin/students"
          icon={<GraduationCap size={18} />}
          label="Students"
        />
        <NavItem
          to="/admin/courses"
          icon={<BookOpen size={18} />}
          label="Courses"
        />
        <NavItem
          to="/admin/reports"
          icon={<FileText size={18} />}
          label="Reports"
        />
        <NavItem
          to="/admin/audit-logs"
          icon={<ClipboardList size={18} />}
          label="Audit Logs"
        />
        <NavItem
          to="/admin/payments"
          icon={<CreditCard size={18} />}
          label="Payments"
        />
        <NavItem
          to="/admin/revenue"
          icon={<CreditCard size={18} />} // Can use Banknote or similar if available, but let's stick to simple
          label="Financials"
        />

        <div className="admin-divider" />

        <NavItem
          to="/admin/profile"
          icon={<UserCog size={18} />}
          label="Profile"
        />
        <NavItem
          to="/settings"
          icon={<Settings size={18} />}
          label="Settings"
        />
      </nav>

      {/* ===== LOGOUT ===== */}
      <button className="admin-logout" onClick={logout}>
        <LogOut size={18} />
        Logout
      </button>
    </aside>
  );
};

const NavItem = ({ to, icon, label }) => (
  <NavLink
    to={to}
    end
    className={({ isActive }) => `admin-nav-item ${isActive ? "active" : ""}`}
  >
    {icon}
    <span>{label}</span>
  </NavLink>
);

export default AdminSidebar;
