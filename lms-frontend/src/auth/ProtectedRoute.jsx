import { Navigate, Outlet } from "react-router-dom";
import { getAuthUser } from "./auth";

const ProtectedRoute = ({ allowedRoles }) => {
  const auth = getAuthUser();

  if (!auth) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(auth.role)) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute; // ✅ THIS LINE WAS MISSING
