# Viva Voce Preparation Guide & Comprehensive Q&A

This guide covers all key viva concepts specified in the project syllabus with precise, clear, and high-scoring answers.

---

## 1. Quick Tool Cheat-Sheet: Why We Used Each Tool & Lifecycle Fit

| Tool | Why We Used It | Lifecycle Fit |
| :--- | :--- | :--- |
| **Git / GitHub** | Distributed version control with branching models (`develop`, `feature/*`), pull request code reviews, and commit auditability. | **Plan & Code** |
| **Java 21 / Spring Boot 3** | Robust, enterprise-grade MVC backend with built-in embedded server, dependency injection, and JPA repository abstractions. | **Code & Package** |
| **Thymeleaf** | Server-side templating engine that renders dynamic HTML views with zero external frontend build complexity. | **Presentation** |
| **H2 Database** | Fast, zero-configuration in-memory/file SQL database that eliminates external DBMS install hurdles while maintaining standard SQL/JPA integrity. | **Data Persistence** |
| **Maven** | Standardized build lifecycle management, dependency resolution, compile, test, and package phases. | **Build Automation** |
| **JUnit 5 / MockMvc** | Automated unit tests and fast in-memory HTTP endpoint testing for business logic and controller routes. | **Continuous Testing** |
| **Selenium WebDriver 4** | End-to-end automated UI regression testing in headless Chrome/Edge to ensure user journeys never break. | **Automated QA Gate** |
| **Jenkins** | Open-source CI/CD automation server orchestrating declarative pipeline stages from checkout to deployment. | **Continuous Integration** |
| **Docker** | Containerization technology providing immutable, portable application packaging across environments. | **Packaging & Shipping** |
| **Docker Compose** | Multi-container definition tool for spinning up interconnected services (App, Tomcat, Jenkins) with a single command. | **Local Orchestration** |
| **Ansible** | Agentless configuration management tool using declarative YAML playbooks to ensure server state idempotency. | **Configuration Management** |
| **Spring Boot Actuator** | Production-ready telemetry and health monitoring endpoints (`/actuator/health`) for automated deployment gating. | **Observability & Health** |

---

## 2. Core Viva Questions & Expert Answers

### Q1: What is the difference between CI (Continuous Integration), CD (Continuous Delivery), and CD (Continuous Deployment)?
- **Continuous Integration (CI):** Developers frequently merge code into a central repository. Every push triggers an automated build and test cycle (JUnit, linting, Selenium). The goal is to detect integration defects early.
- **Continuous Delivery (CD):** An extension of CI where code changes are automatically tested and packaged into deployable artifacts (e.g., Docker images or JARs) and staged for deployment. Release to production requires manual human approval.
- **Continuous Deployment (CD):** Fully automated pipeline where every change that passes all quality gates is automatically pushed to production environments without human intervention.

---

### Q2: Walk through each stage of your `Jenkinsfile` and explain its purpose.
1. **Checkout SCM:** Clones the Git repository and prints recent commit metadata for audit tracking.
2. **Build & Unit Test:** Executes `mvn clean compile test` to verify class compilation and test business logic with JUnit 5 and MockMvc.
3. **Package Artifact:** Runs `mvn package` to build the self-contained executable `.jar`/`.war` bundle.
4. **Selenium UI Test Gate:** Spins up the application and runs headless Chrome browser tests. If any assertion fails, a screenshot is captured to `screenshots/` and the pipeline terminates immediately with an error.
5. **Archive Artifacts:** Saves the verified build JAR as a persistent Jenkins artifact with SHA fingerprints.
6. **Deploy to Tomcat / Staging:** Deploys the artifact to the designated target environment configured via parameter `DEPLOY_ENV`.
7. **Health Check Verification:** Sends an HTTP `GET /health` request to verify the deployed service is healthy (`{"status": "UP"}`).

---

### Q3: Why must automated tests gate deployment? What happens if you skip this gate?
- **Quality Gating Principle:** Tests prevent "defect leakage". If a defective commit reaches production, it causes service downtime, data corruption, or donor distrust.
- By configuring the pipeline to abort downstream deployment stages upon test failure, we guarantee that only code passing 100% of unit, integration, and UI acceptance criteria is ever packaged and shipped.
- In our project, we demonstrated this by injecting a test failure: the pipeline turned red, blocked the Docker build and Tomcat deployment, and archived a failure screenshot. Once fixed, the green pipeline deployed cleanly.

---

### Q4: What is the difference between a Docker Image and a Docker Container?
- **Docker Image:** An immutable, read-only template or blueprint containing the application binaries, libraries, runtime dependencies, and environment variables. Built from a `Dockerfile` and stored in a registry (e.g., Docker Hub).
- **Docker Container:** A live, runnable instance of a Docker image. It adds a thin read-write layer on top of the immutable image layers and runs as an isolated process on the host operating system kernel using cgroups and namespaces.

---

### Q5: Why did we use a Multi-Stage Dockerfile?
- A standard single-stage Dockerfile leaves Maven, the Java compiler (JDK), source code, and intermediate build cache inside the final image, resulting in image sizes exceeding 800 MB and expanding the security attack surface.
- A **Multi-Stage Dockerfile** separates the *Build environment* from the *Runtime environment*:
  - **Stage 1 (Maven + JDK):** Compiles and packages the application.
  - **Stage 2 (Eclipse Temurin JRE):** Copies only the resulting `app.jar` into a minimal, hardened JRE container (~200 MB), run by an unprivileged non-root user `spring`.

---

### Q6: What is Idempotency in Ansible and why is it important?
- **Definition:** An operation is **idempotent** if applying it multiple times produces the exact same end state as applying it once, without causing unintended side effects or duplicate modifications.
- **Why it matters:** In traditional shell scripts, re-running a script might append lines to configuration files multiple times or fail because a folder already exists. Ansible checks the current state against the desired state:
  - **Run #1:** Creates user, creates directories, starts container &rarr; reports `changed=6`.
  - **Run #2:** Detects user already exists, directories exist, container is already running with desired image &rarr; reports `changed=0, ok=8`.
- This eliminates configuration drift and ensures reproducible infrastructure.

---

### Q7: Explain your Git branching strategy.
- We follow an agile **Gitflow** model:
  - `main`: Protected production branch containing only audited, tagged releases (`v1.0.0`).
  - `develop`: Integration branch where all completed feature branches merge.
  - `feature/*`: Short-lived branches dedicated to individual tasks (`feature/data-entry`, `feature/dashboard`).
- Features are merged into `develop` through Pull Requests after peer review and CI validation. When ready for release, `develop` is merged to `main` with a release tag.

---

### Q8: How does the Rollback mechanism work in your pipeline?
- If a post-deployment health check (`/health`) fails or container crashes:
  1. The monitoring/Ansible playbook detects non-200 HTTP response.
  2. The `ansible/rollback.yml` playbook executes automatically.
  3. It stops and removes the degraded container.
  4. It pulls and launches the previously tagged stable release container (e.g. `ngo-project-dashboard:1.0.0-stable`).
  5. It performs a health check to verify service restoration.
- This bounds recovery time to a few seconds (Mean Time To Recovery &lt; 5s).
