# DevOps Lifecycle Architecture & Flow Diagram

This document illustrates the end-to-end continuous integration, continuous testing, containerization, and configuration management lifecycle for the **NGO Project Progress Dashboard**.

---

## 1. DevOps Lifecycle Diagram (Mermaid)

```mermaid
flowchart TD
    subgraph Plan_and_Develop ["1. Plan & Code"]
        A[Backlog / User Stories] --> B[Feature Branch Git Checkout]
        B --> C[Spring Boot MVC Code & Thymeleaf]
        C --> D[Local JUnit & MockMvc Tests]
    end

    subgraph Version_Control ["2. Version Control (Git)"]
        D --> E[Git Commit & Push feature/*]
        E --> F[Open Pull Request into develop]
        F --> G[Code Review & Automated CI Trigger]
    end

    subgraph CI_Pipeline ["3. Continuous Integration (Jenkins)"]
        G --> H[Stage 1: Checkout SCM]
        H --> I[Stage 2: Maven Compile & Unit Tests]
        I --> J[Stage 3: Maven Package .jar/.war]
        J --> K[Stage 4: Headless Selenium UI Tests]
        K -->|Test Failed| L[Capture Failure Screenshot & Block Pipeline]
        K -->|All Tests Pass| M[Archive Build Artifact]
    end

    subgraph Containerization ["4. Containerization (Docker)"]
        M --> N[Build Multi-Stage Docker Image]
        N --> O[Tag Versioned Image 1.0.BUILD_NUMBER]
        O --> P[Push to Registry / Docker Hub]
    end

    subgraph Deployment_and_Ops ["5. Deploy & Config Management (Ansible)"]
        P --> Q[Ansible Inventory & Target Server]
        Q --> R[Playbook: Install Pre-reqs & Docker]
        R --> S[Playbook: Pull & Run NGO Container]
        S --> T[Automated Health Check: curl /health]
        T -->|Health UP| U[Deployment Succeeded & Idempotent Verify]
        T -->|Health DOWN| V[Automated Rollback to Previous Image]
    end

    style CI_Pipeline fill:#f0f9ff,stroke:#0284c7,stroke-width:2px
    style Containerization fill:#ecfdf5,stroke:#059669,stroke-width:2px
    style Deployment_and_Ops fill:#fefce8,stroke:#ca8a04,stroke-width:2px
```

---

## 2. Stage Breakdown & DevOps Principles

| Stage | Tooling | DevOps Principle | Quality Gate Criteria |
| :--- | :--- | :--- | :--- |
| **Plan & Track** | GitHub Projects / Kanban | Agile Transparency & WIP Limits | Definition of Ready satisfied. |
| **Code & Commit** | Git, Java 21, Spring Boot | Version Control & Traceability | Conventional commits (`feat:`, `fix:`). |
| **Continuous Integration** | Jenkins, Maven | Fast Feedback Loop | Clean build, zero compiler warnings. |
| **Automated Testing** | JUnit 5, Selenium, Chrome | Quality Gating | 100% test pass rate; screenshot on failure. |
| **Containerization** | Docker, Docker Compose | Immutable Infrastructure | Minimal image size, unprivileged runner. |
| **Config Management** | Ansible | Idempotency & Infrastructure as Code | Second playbook run reports `changed=0`. |
| **Monitoring & Rollback** | Spring Actuator, Curl | High Availability & Resilience | `/health` returns 200; automated tag rollback. |
