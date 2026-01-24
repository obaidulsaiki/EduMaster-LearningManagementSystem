import React from "react";
import { Globe, Twitter, Linkedin, Facebook, Instagram, Send, Mail } from "lucide-react";
import "./Footer.css";

function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="footer-container">
        
        {/* --- Top Section: Brand & Newsletter --- */}
        <div className="footer-top">
          <div className="footer-brand-section">
            <div className="footer-logo">
              <div className="logo-icon-small">L</div>
              <span className="brand-text">LMS<span className="highlight">AI</span></span>
            </div>
            <p className="brand-desc">
              Empowering the next generation of learners with AI-driven course pathways and personalized mentorship.
            </p>
          </div>

          <div className="footer-newsletter">
            <h4>Stay ahead of the curve</h4>
            <p>Join our newsletter for the latest AI education trends.</p>
            <div className="input-group">
              <Mail className="input-icon" size={18} />
              <input type="email" placeholder="Enter your email" />
              <button className="btn-subscribe">
                <Send size={16} />
              </button>
            </div>
          </div>
        </div>

        <div className="footer-divider"></div>

        {/* --- Middle Section: Links Grid --- */}
        <div className="footer-grid">
          <div className="footer-col">
            <h4>Platform</h4>
            <a href="#">Browse Courses</a>
            <a href="#">Learning Paths</a>
            <a href="#">AI Mentor</a>
            <a href="#">Pricing</a>
          </div>

          <div className="footer-col">
            <h4>Company</h4>
            <a href="#">About Us</a>
            <a href="#">Careers</a>
            <a href="#">Blog</a>
            <a href="#">Partners</a>
          </div>

          <div className="footer-col">
            <h4>Resources</h4>
            <a href="#">Help Center</a>
            <a href="#">Community</a>
            <a href="#">Teaching Center</a>
            <a href="#">Developer API</a>
          </div>

          <div className="footer-col">
            <h4>Legal</h4>
            <a href="#">Terms of Use</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Cookie Settings</a>
            <a href="#">Accessibility</a>
          </div>
        </div>

        <div className="footer-divider"></div>

        {/* --- Bottom Section: Copyright, Socials, Lang --- */}
        <div className="footer-bottom">
          <p className="copyright">
            © {currentYear} LMS-AI Inc. All rights reserved.
          </p>

          <div className="footer-socials">
            <a href="#" aria-label="Twitter"><Twitter size={20} /></a>
            <a href="#" aria-label="LinkedIn"><Linkedin size={20} /></a>
            <a href="#" aria-label="Facebook"><Facebook size={20} /></a>
            <a href="#" aria-label="Instagram"><Instagram size={20} /></a>
          </div>

          <div className="footer-lang">
            <button>
              <Globe size={16} />
              <span>English (US)</span>
            </button>
          </div>
        </div>

      </div>
    </footer>
  );
}

export default Footer;