# Presentation Slide Deck: NGO Project Progress Dashboard
### 15-Week Capstone Project in DevOps Automation

---

## Slide 1: Title & Team Information
- **Project Title:** NGO Project Progress Dashboard
- **Domain:** DevOps Engineering & Web Application Lifecycle Management
- **Key Stack:** Java 21, Spring Boot 3, Maven, Thymeleaf, H2, Jenkins, Docker, Ansible
- **Presented by:** Unnati & DevOps Engineering Team
- **Date:** October 2026

---

## Slide 2: Problem Statement & Motivation
- **The Challenge:** Non-profit NGOs manage mission-critical initiatives (clean water, medical relief, school electrification) across disparate remote locations.
- **Pain Points:** 
  - Data fragmented across spreadsheets and paper logs.
  - Missed project deadlines and budget overruns discovered after deadlines expire.
  - Absence of automated testing and deployment pipelines leading to fragile production releases.
- **Our Solution:** A high-integrity web application coupled with a fully automated, gated CI/CD and Configuration Management pipeline.

---

## Slide 3: Application Architecture
- **Layered Enterprise Pattern:**
  - **Presentation Layer:** Responsive Thymeleaf views, Bootstrap 5 UI, Real-time search.
  - **Service & Business Logic:** Milestone velocity recalculation, overdue detection, budget burn rates.
  - **Persistence Layer:** Spring Data JPA with embedded H2 in-memory/file database.
  - **DevOps Observability:** Health check endpoint (`/health`) and Spring Actuator metrics (`/actuator/health`).

---

## Slide 4: Agile Workflow & Git Branching Strategy
- **Kanban Board:** 5-stage lifecycle (`Backlog` &rarr; `In Progress` &rarr; `Code Review` &rarr; `Test/QA` &rarr; `Done`).
- **Branching Hierarchy:**
  - `main`: Protected production branch, release tagged (`v1.0.0`).
  - `develop`: Integration branch for CI builds.
  - `feature/*`: Dedicated branches (`feature/data-entry`, `feature/dashboard`).
- **Merge Conflict Resolution:** Demonstrated intentional branch divergence, collision detection, and clean manual resolution.

---

## Slide 5: Core Application Features & Visual Tour
- **Executive Portfolio Dashboard:** Real-time KPI summary (Total, Completed, In-Progress, Delayed, Beneficiaries, Budget).
- **Interactive Milestone Tracker:** Add milestones per project, toggle completion with auto-progress recalculation.
- **Proactive Early-Warning Risk Center:** Dedicated `/alerts` screen highlighting initiatives with < 50% progress under 30 days.
- **Full Database Auditing:** Built-in H2 web console for immediate schema and row verification.

---

## Slide 6: Continuous Integration with Jenkins
- **Declarative Pipeline Stages:**
  1. SCM Checkout & Commit Metadata Inspection
  2. Maven Compilation & JUnit 5 Unit Tests
  3. Artifact Packaging (`.jar`/`.war`)
  4. Selenium UI Quality Gate
  5. Artifact Archiving & Fingerprinting
  6. Parameterized Deploy to Tomcat/Staging
  7. Automated Health Check Verification

---

## Slide 7: Automated Testing & Deployment Quality Gate
- **Testing Pyramid:**
  - **JUnit 5:** Business logic and mathematical progress calculations.
  - **MockMvc:** Controller routing, HTTP status codes, and form validation.
  - **Selenium WebDriver 4:** 4 headless Chrome browser tests verifying real user workflows.
- **Failure Screenshot Hook:** Automatically captures PNG evidence upon any UI test failure.
- **Gating Mechanism:** Any test failure stops downstream deployment stages, preventing defective code from reaching staging.

---

## Slide 8: Containerization with Docker
- **Multi-Stage Build Architecture:**
  - *Build Stage:* Maven 3.9 + Temurin JDK 21 compiles application.
  - *Runtime Stage:* Eclipse Temurin 21 JRE Jammy (only 210 MB, non-root user `spring`).
- **Portability:** Consistent runtime across Windows, macOS, Linux, and Cloud instances.
- **Container Lifecycle:** Defined startup, shutdown, restart, and healthcheck probes in `docker-compose.yml`.

---

## Slide 9: Configuration Management with Ansible
- **Infrastructure as Code (IaC):**
  - Declarative YAML playbook managing packages, users, folders, and container lifecycle.
- **Idempotency Demonstration:**
  - **Run #1:** Initial state application (`changed=6, ok=8`).
  - **Run #2:** Re-run on configured server (`changed=0, ok=8`) &rarr; Idempotency Confirmed!
- **Automated Rollback:** Reverts to previously tagged stable image in < 2 seconds if health check fails.

---

## Slide 10: Conclusion & Key Learnings
- **DevOps Capstone Outcomes:**
  - Built and verified a full-lifecycle enterprise solution in a single cohesive project.
  - Achieved 100% automated test coverage across unit, integration, and UI layers.
  - Demonstrated automated quality gates, container packaging, and idempotent deployment.
- **Future Enhancements:**
  - Kubernetes Helm chart deployment.
  - Prometheus & Grafana telemetry dashboards.
  - Multi-tenant cloud donor portals.
