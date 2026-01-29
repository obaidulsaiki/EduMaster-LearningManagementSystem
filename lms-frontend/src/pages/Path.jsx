import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { 
  Code, Server, Database, Smartphone, Cloud, Shield, 
  Cpu, Layout, Globe, Activity, Layers, PenTool, 
  Gamepad, Box, Zap, Search, Brain, Share2, 
  Wifi, BarChart, Terminal, X, ChevronRight, CheckCircle2
} from "lucide-react";
import "./Path.css";

const roadmaps = [
  {
    id: "software-engineer",
    title: "Software Engineer (Generalist)",
    icon: <Code />,
    description: "The ultimate foundational path for building robust software systems from scratch.",
    steps: [
      { title: "Phase 1: CS Fundamentals", skills: ["Learn a Language (C++/Java/Python)", "Discrete Mathematics", "Data Structures", "Algorithms (Recursion, Sorting, Searching)"] },
      { title: "Phase 2: Computer Systems", skills: ["Operating Systems (Process, Threads, Memory)", "Computer Networks (TCP/UDP, DNS, HTTP)", "Database Management Systems (Normalization, SQL)"] },
      { title: "Phase 3: Professional Tooling", skills: ["Version Control (Git/GitHub)", "Command Line (Unix/Linux shell)", "Environment Setup (Docker, VMs)"] },
      { title: "Phase 4: Low-Level Design (LLD)", skills: ["Object-Oriented Programming (OOP)", "SOLID Principles", "Design Patterns (Singleton, Factory, Observer)"] },
      { title: "Phase 5: High-Level Design (HLD)", skills: ["Load Balancing", "Horizontal vs Vertical Scaling", "Microservices vs Monolith", "Caching & CDN"] },
      { title: "Phase 6: Project Lifecycle", skills: ["Testing (Unit, Integration)", "Agile Methodologies", "CI/CD Pipelines", "Code Reviews"] }
    ]
  },
  {
    id: "frontend",
    title: "Frontend Developer",
    icon: <Layout />,
    description: "Master the art of creating responsive and interactive user experiences for the modern web.",
    steps: [
      { title: "Lvl 1: The Basics (The Web Trio)", skills: ["HTML5 (Semantic, SEO)", "CSS3 (Layouts, Selectors)", "JavaScript (DOM, ES6 Syntax)"] },
      { title: "Lvl 2: Advanced Styling", skills: ["Responsive Design", "Flexbox & Grid", "Sass/Less", "Tailwind CSS", "CSS Modules"] },
      { title: "Lvl 3: JS Mastery", skills: ["Asynchronous JS (Promises, Async/Await)", "Fetch API/Axios", "Modules", "Closures & Prototypes"] },
      { title: "Lvl 4: Modern Frameworks", skills: ["React.js (Hooks, Virtual DOM)", "Next.js (SSR, ISR, Routing)", "TypeScript (Static Typing)"] },
      { title: "Lvl 5: State & Data", skills: ["Redux Toolkit / Zustand", "Context API", "React Query / SWR"] },
      { title: "Lvl 6: Testing & Performance", skills: ["Jest & RTL", "Performance Optimization (Lighthouse)", "Accessibility (a11y)"] }
    ]
  },
  {
    id: "backend",
    title: "Backend Developer",
    icon: <Server />,
    description: "Build the engines that power complex applications, focus on logic, databases, and scale.",
    steps: [
      { title: "Phase 1: Choose a Runtime", skills: ["Node.js (Express/Nest)", "Java (Spring Boot)", "Go (Fiber/Gin)", "Python (Django/FastAPI)"] },
      { title: "Phase 2: Database Layer", skills: ["Relational (PostgreSQL, MySQL)", "Non-Relational (MongoDB, Cassandra)", "Indexing", "Transactions"] },
      { title: "Phase 3: API Architecture", skills: ["RESTful Standards", "GraphQL", "WebSockets (Socket.io)", "gRPC"] },
      { title: "Phase 4: Security & Authentication", skills: ["OAuth2 / JWT", "Password Hashing (Argon2)", "CORS/XSS/CSRF Prevention"] },
      { title: "Phase 5: Infrastructure & Deploys", skills: ["Docker", "Nginx / Reverse Proxies", "Message Queues (RabbitMQ/Kafka)"] },
      { title: "Phase 6: Advanced Backend", skills: ["Serverless Architecture", "Distributed Tracing", "Kubernetes Management"] }
    ]
  },
  {
    id: "fullstack",
    title: "Full-Stack Developer",
    icon: <Layers />,
    description: "The complete package. Handle everything from database design to UI implementation.",
    steps: [
      { title: "Step 1: Frontend Professional", skills: ["React", "HTML/CSS Mastery", "Client Optimization"] },
      { title: "Step 2: Backend Mastery", skills: ["Server logic", "REST/GraphQL", "User Management"] },
      { title: "Step 3: Database & Cloud", skills: ["SQL/NoSQL Mix", "AWS/Vercel/DigitalOcean", "Firebase/Supabase"] },
      { title: "Step 4: Integration Mastery", skills: ["Testing across the stack", "Webhooks", "Real-time sync"] }
    ]
  },
  {
    id: "ai-engineer",
    title: "AI & ML Engineer",
    icon: <Brain />,
    description: "Step into the future by building intelligent systems and master the LLM ecosystem.",
    steps: [
      { title: "Stage 1: Python & Math", skills: ["Python (Numpy/Pandas)", "Linear Algebra", "Calculus", "Basic Statistics"] },
      { title: "Stage 2: Machine Learning", skills: ["Scikit-learn", "Regression/Classification", "Clustering", "Neural Networks"] },
      { title: "Stage 3: Deep Learning", skills: ["PyTorch / TensorFlow", "CNNs / RNNs", "Computer Vision Basics"] },
      { title: "Stage 4: Natural Language (NLP)", skills: ["Transformers", "Text Processing", "Tokenization"] },
      { title: "Stage 5: Gen AI & LLMs", skills: ["LangChain", "OpenAI / HuggingFace", "Vector DBs (Pinecone, Chroma)", "RAG Architecture"] },
      { title: "Stage 6: MLOps", skills: ["Model Deployment", "Monitoring", "Version Control for Data (DVC)"] }
    ]
  },
  {
    id: "devops",
    title: "DevOps Engineer",
    icon: <Activity />,
    description: "Automate delivery and maintain high availability of mission-critical systems.",
    steps: [
      { title: "Phase 1: General Tooling", skills: ["Linux Internal", "Networking protocols", "SSH", "Bash/Python Scripting"] },
      { title: "Phase 2: Configuration Management", skills: ["Ansible", "Chef", "Puppet"] },
      { title: "Phase 3: CI/CD Mastery", skills: ["GitHub Actions", "Jenkins", "GitLab CI", "ArgoCD"] },
      { title: "Phase 4: Containerization", skills: ["Docker (Images, Compose)", "Kubernetes (Pods, Services, Ingress)"] },
      { title: "Phase 5: Cloud Engineering", skills: ["Infrastructure as Code (Terraform, Pulumi)", "AWS/Azure/GCP"] },
      { title: "Phase 6: Monitoring & Security", skills: ["Prometheus/Grafana", "ELK Stack", "DevSecOps (Snyk, Aqua)"] }
    ]
  },
  {
    id: "cybersecurity",
    title: "Cybersecurity Expert",
    icon: <Shield />,
    description: "Offense and defense. Protect the digital world from evolving threats.",
    steps: [
      { title: "Step 1: IT Fundamentals", skills: ["Networking (OSI Model)", "CompTIA A+ Content", "Linux Administration"] },
      { title: "Step 2: Security Core", skills: ["Cryptography", "Identity Access Management", "Firewalls/VPNs"] },
      { title: "Step 3: Ethical Hacking", skills: ["Penetration Testing", "Burp Suite", "Kali Linux", "THM/HTB Practice"] },
      { title: "Step 4: Digital Forensics", skills: ["Malware Analysis", "Memory Forensics", "Incident Response"] },
      { title: "Step 5: GRC (Governance & Risk)", skills: ["SOC2", "GDPR/HIPAA", "Risk Assessment"] }
    ]
  },
  {
    id: "blockchain",
    title: "Blockchain Engineer",
    icon: <Layers />,
    description: "Build the next generation of decentralized web (Web3).",
    steps: [
      { title: "Path 1: Blockchain Basics", skills: ["Cryptography", "P2P Systems", "Consensus Algorithms (PoW/PoS)"] },
      { title: "Path 2: Smart Contracts", skills: ["Solidity (Ethereum)", "Rust (Solana/Polkadot)", "Vyper"] },
      { title: "Path 3: Development Tools", skills: ["Hardhat / Foundry", "Truffle", "Ganache", "Remix"] },
      { title: "Path 4: DApp Integration", skills: ["Ethers.js / Web3.js", "IPFS", "The Graph"] }
    ]
  },
  {
    id: "game-dev",
    title: "Game Developer",
    icon: <Gamepad />,
    description: "Bring imagination to life through high-performance game engines.",
    steps: [
      { title: "Beginner: Coding for Games", skills: ["C# (for Unity) or C++ (for Unreal)", "Math for Games (Vector, Tracer)"] },
      { title: "Intermediate: Core Mechanics", skills: ["Game Engine Workflow", "Physics Engines", "Behavior Trees / AI"] },
      { title: "Advanced: Visuals & Sound", skills: ["Shader Programming (HLSL/GLSL)", "3D Animation Pipelines", "Spatial Audio"] },
      { title: "Pro: Optimization & Network", skills: ["Memory Management", "Multiplayer Sync", "Console Porting"] }
    ]
  },
  {
    id: "data-scientist",
    title: "Data Scientist",
    icon: <BarChart />,
    description: "Tell stories through data. Master analytics, visualization, and prediction.",
    steps: [
      { title: "Phase 1: Statistical Foundatons", skills: ["Descriptive Stats", "Hypothesis Testing", "Probability Distributions"] },
      { title: "Phase 2: Coding & Querying", skills: ["Python for Data (Pandas/Polars)", "Advanced SQL", "R Language"] },
      { title: "Phase 3: Visualization", skills: ["Tableau / PowerBI", "Plotly / Seaborn", "Storytelling with Data"] },
      { title: "Phase 4: ML Integration", skills: ["Regression Modeling", "Classification", "Feature Engineering"] }
    ]
  },
  {
    id: "product-manager",
    title: "Product Manager (Technical)",
    icon: <Globe />,
    description: "Navigate high-level strategy and technical constraints to lead teams.",
    steps: [
      { title: "Core 1: Product Mindset", skills: ["User Research", "MVP Strategy", "Metrics (North Star, Pirate)"] },
      { title: "Core 2: Technical depth", skills: ["System Architecture for non-engineers", "API Basics", "SDLC"] },
      { title: "Core 3: Execution", skills: ["Agile/Scrum", "PRD Writing", "Stakeholder Mgmt"] }
    ]
  },
  {
    id: "site-reliability",
    title: "SRE (Site Reliability Engineer)",
    icon: <Zap />,
    description: "Engineers specialized in system scalability and rock-solid reliability.",
    steps: [
      { title: "Foundations", skills: ["Software Engineering background", "Deep Linux systems", "Distributed Systems Theory"] },
      { title: "SRE Core", skills: ["SLIs/SLOs/SLAs", "Error Budgets", "Automation over Toil"] },
      { title: "Tooling", skills: ["Go programming", "Terraform", "Kubernetes Operator pattern"] }
    ]
  },
  {
    id: "data-engineer",
    title: "Data Engineer",
    icon: <Database />,
    description: "Design the pipelines and infrastructure that transport massive datasets.",
    steps: [
      { title: "Phase 1: Programming & Linux", skills: ["Python/Scala", "Linux Scripting", "SQL Mastery"] },
      { title: "Phase 2: Big Data Systems", skills: ["Apache Spark", "Hadoop ecosystem", "Kafka (Streaming)"] },
      { title: "Phase 3: Data Orchestration", skills: ["Airflow", "dbt", "Prefect"] },
      { title: "Phase 4: Cloud Warehousing", skills: ["Snowflake", "Databricks", "BigQuery"] }
    ]
  },
  {
    id: "mobile-generalist",
    title: "Mobile Dev (Cross-Platform)",
    icon: <Smartphone />,
    description: "Build high-quality apps for both Android and iOS with one codebase.",
    steps: [
      { title: "Phase 1: Languages", skills: ["Dart (for Flutter)", "JavaScript/TypeScript (for React Native)"] },
      { title: "Phase 2: Framework Mastery", skills: ["Flutter / React Native", "State Mgmt (Riverpod/Redux)"] },
      { title: "Phase 3: Native Bridge", skills: ["Platform Channels", "Native Modules", "Kotlin/Swift basics"] }
    ]
  },
  {
    id: "qa-automation",
    title: "QA Automation Engineer",
    icon: <CheckCircle2 />,
    description: "The gatekeeper of Quality. Build robust automated testing suites.",
    steps: [
      { title: "Step 1: Testing Theory", skills: ["Manual Testing Foundation", "Bug Lifecycle", "Test Case design"] },
      { title: "Step 2: Scripting", skills: ["JavaScript / Python / Java", "CSS/XPath Selectors"] },
      { title: "Step 3: Web Automation", skills: ["Selenium", "Playwright", "Cypress"] },
      { title: "Step 4: Advanced Testing", skills: ["API testing (Postman/Newman)", "Mobile Testing (Appium)"] }
    ]
  },
  {
    id: "embedded",
    title: "Embedded Systems",
    icon: <Cpu />,
    description: "Where code meets hardware. Program microchips, IoT, and high-tech devices.",
    steps: [
      { title: "Lvl 1: C/C++ Mastery", skills: ["Memory management", "Pointers", "Bitwise operations"] },
      { title: "Lvl 2: Hardware Basics", skills: ["Microcontrollers (STM32/ESP32)", "Assembly basics"] },
      { title: "Lvl 3: Communications", skills: ["UART / SPI / I2C", "CAN Bus", "Networking for IoT"] }
    ]
  },
  {
    id: "cloud-engineer",
    title: "Cloud Support & Ops",
    icon: <Cloud />,
    description: "The experts in managing and scaling multi-cloud environments.",
    steps: [
      { title: "Phase 1: Cloud Core", skills: ["AWS Certified Solution Architect path", "IAM & VPCs"] },
      { title: "Phase 2: Modern Ops", skills: ["K8s administration", "Serverless management"] },
      { title: "Phase 3: Cost & Security", skills: ["FinOps", "Config monitoring", "Governance"] }
    ]
  },
  {
    id: "data-analyst",
    title: "Data Analyst",
    icon: <BarChart />,
    description: "Interpreting complex data to provide business insights.",
    steps: [
      { title: "Skills 1", skills: ["Excel Expert", "SQL for analysis", "Statistics"] },
      { title: "Skills 2", skills: ["Data Cleaning", "Tableau/Looker", "A/B Testing analysis"] }
    ]
  },
  {
    id: "uiux-designer",
    title: "UI/UX Designer (Tech Focus)",
    icon: <PenTool />,
    description: "Design the look, feel, and flow of digital products.",
    steps: [
      { title: "Foundations", skills: ["Visual Design", "Color Theory", "Typography"] },
      { title: "UX Mastery", skills: ["User Flow", "Wireframing", "Prototyping in Figma/Adobe XD"] },
      { title: "Tech Connection", skills: ["Design Systems", "Component-driven design", "Hand-off docs"] }
    ]
  },
  {
    id: "ar-vr-dev",
    title: "AR/VR Developer",
    icon: <Box />,
    description: "Build immersive experiences for the next era of computing.",
    steps: [
      { title: "Lvl 1", skills: ["Unity/Unreal Engine", "C# / C++", "3D Math"] },
      { title: "Lvl 2", skills: ["XR Interactions", "Oculus SDK", "Apple VisionPro dev"] }
    ]
  },
  {
    id: "nlp-specialist",
    title: "NLP Specialist",
    icon: <Activity />,
    description: "Go deep into Natural Language Processing and text analytics.",
    steps: [
      { title: "Core", skills: ["Linguistics Basics", "Word Embeddings", "Transformers Architecture"] },
      { title: "State of Art", skills: ["LLM Fine-tuning", "Tokenization strategies", "Ethical AI"] }
    ]
  }
];

const Path = () => {
  const [selectedPath, setSelectedPath] = useState(null);

  const containerVariants = {
    hidden: { opacity: 0 },
    show: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    show: { opacity: 1, y: 0 }
  };

  return (
    <div className="path-page">
      <div className="path-container">
        
        <motion.div 
          className="path-header"
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <h1>The CSE Career Matrix</h1>
          <p>Exhaustive, industry-standard roadmaps for every significant role in the computer science ecosystem. From zero knowledge to professional mastery.</p>
        </motion.div>

        <motion.div 
          className="path-grid"
          variants={containerVariants}
          initial="hidden"
          animate="show"
        >
          {roadmaps.map((path) => (
            <motion.div 
              key={path.id} 
              className="path-card"
              variants={itemVariants}
              onClick={() => setSelectedPath(path)}
              whileHover={{ 
                scale: 1.03,
                transition: { duration: 0.2 }
              }}
              whileTap={{ scale: 0.98 }}
            >
              <div className="path-card-icon">{path.icon}</div>
              <h3>{path.title}</h3>
              <p>{path.description}</p>
              <div className="path-card-footer">
                <span>View Full Roadmap</span>
                <ChevronRight size={16} />
              </div>
            </motion.div>
          ))}
        </motion.div>

        <AnimatePresence>
          {selectedPath && (
            <motion.div 
              className="path-overlay"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setSelectedPath(null)}
            >
              <motion.div 
                className="path-modal"
                initial={{ scale: 0.9, y: 50, opacity: 0 }}
                animate={{ scale: 1, y: 0, opacity: 1 }}
                exit={{ scale: 0.9, y: 50, opacity: 0 }}
                onClick={(e) => e.stopPropagation()}
              >
                <button className="close-modal" onClick={() => setSelectedPath(null)}>
                  <X />
                </button>

                <div className="roadmap-header">
                  <div className="path-card-icon" style={{ margin: "0 0 20px 0" }}>
                    {selectedPath.icon}
                  </div>
                  <h2>{selectedPath.title}</h2>
                  <p className="detailed-desc">{selectedPath.description}</p>
                </div>

                <div className="roadmap-content">
                  {selectedPath.steps.map((step, idx) => (
                    <motion.div 
                      key={idx} 
                      className="roadmap-step"
                      initial={{ opacity: 0, x: -30 }}
                      animate={{ opacity: 1, x: 0 }}
                      transition={{ delay: 0.2 + idx * 0.1 }}
                    >
                      <div className="step-indicator">{idx + 1}</div>
                      <h4>{step.title}</h4>
                      <div className="skills-tags">
                        {step.skills.map((skill, sIdx) => (
                          <span key={sIdx} className="skill-tag">{skill}</span>
                        ))}
                      </div>
                    </motion.div>
                  ))}
                </div>

                <div className="modal-footer-cta">
                  <p>Ready to start this journey? Check out our <strong>{selectedPath.title}</strong> courses.</p>
                  <button onClick={() => window.location.href = "/browse"}>Explore Courses</button>
                </div>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>

      </div>
    </div>
  );
};

export default Path;
