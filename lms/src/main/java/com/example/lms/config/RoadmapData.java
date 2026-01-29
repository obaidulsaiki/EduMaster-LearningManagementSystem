package com.example.lms.config;

import java.util.HashMap;
import java.util.Map;

public class RoadmapData {

    public static final Map<String, String> ROADMAPS = new HashMap<>();

    static {
        ROADMAPS.put("Software Engineer", """
                Steps:
                1. Phase 1: CS Fundamentals (DS/Algo, OS, Networking)
                2. Phase 2: Computer Systems (TCP/UDP, DNS, HTTP, DBMS)
                3. Phase 3: Professional Tooling (Git, Docker, Linux Shell)
                4. Phase 4: LLD (OOP, SOLID, Design Patterns)
                5. Phase 5: HLD (Scaling, Microservices, Caching)
                """);

        ROADMAPS.put("Frontend Developer", """
                Steps:
                1. Lvl 1: Basics (HTML5, CSS3, ES6 JavaScript)
                2. Lvl 2: Advanced Styling (Flexbox, Grid, Tailwing)
                3. Lvl 3: JS Mastery (Async/Await, Promises, Closures)
                4. Lvl 4: Frameworks (React.js, Next.js, TypeScript)
                5. Lvl 5: State (Redux, Zustand, Context API)
                """);

        ROADMAPS.put("Backend Developer", """
                Steps:
                1. Phase 1: Language/Runtime (Node.js, Java Spring, Python Django)
                2. Phase 2: Database Layer (PostgreSQL, MongoDB, Indexing)
                3. Phase 3: API Architecture (REST, GraphQL, gRPC)
                4. Phase 4: Security (OAuth2, JWT, Hashing)
                5. Phase 5: Infrastructure (Docker, Nginx, Kafka)
                """);

        ROADMAPS.put("AI & ML Engineer", """
                Steps:
                1. Stage 1: Math (Linear Algebra, Stats, Python)
                2. Stage 2: Machine Learning (Scikit-learn, Regression)
                3. Stage 3: Deep Learning (PyTorch, CNNs, Vision)
                4. Stage 4: NLP (Transformers, Tokenization)
                5. Stage 5: Gen AI & LLMs (LangChain, OpenAI, RAG)
                """);

        ROADMAPS.put("DevOps Engineer", """
                Steps:
                1. Phase 1: Linux & Scripting (Bash, Python, Networking)
                2. Phase 2: Configuration (Ansible, SSH)
                3. Phase 3: CI/CD (GitHub Actions, Jenkins, ArgoCD)
                4. Phase 4: Containers (Docker, Kubernetes)
                5. Phase 5: Cloud (Terraform, AWS/Azure/GCP)
                """);

        ROADMAPS.put("Cybersecurity Expert", """
                Steps:
                1. Step 1: IT Fundamentals (Networking, Linux Admin)
                2. Step 2: Security Core (Cryptography, IAM, Firewalls)
                3. Step 3: Ethical Hacking (PenTesting, Kali Linux)
                4. Step 4: Digital Forensics (Malware Analysis)
                5. Step 5: GRC (SOC2, GDPR, Risk Assessment)
                """);

        // ... (Adding a general summary for other 15+ roles)
        ROADMAPS.put("SRE/Blockchain/Mobile/DataEngineer/GameDev/ProductManager",
                "Multiple specialized paths including Mobile (Flutter/RN), Blockchain (Web3/Solidity), " +
                        "SRE (Site Reliability), Game Dev (Unity/C#), Data Engineering (Spark/Kafka), and UI/UX Design.");
    }

    public static String getRoadmapContext() {
        StringBuilder sb = new StringBuilder("INDUSTRY ROADMAPS:\n");
        ROADMAPS.forEach((role, steps) -> {
            sb.append("Role: ").append(role).append("\n").append(steps).append("\n");
        });
        return sb.toString();
    }
}
