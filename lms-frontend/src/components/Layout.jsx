// src/components/Layout.jsx
import Navbar from "./Navbar";
import Footer from "./Footer";
import { Outlet } from "react-router-dom";

const Layout = () => {
  return (
    <div className="app-layout">
      <Navbar />

      {/* Push content below fixed navbar */}
      <main className="app-content">
        <Outlet />
      </main>

      <Footer />
    </div>
  );
};

export default Layout;
