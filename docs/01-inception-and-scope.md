# Project Inception & Scope: NGO Project Progress Dashboard

## 1. Executive Summary
Non-Governmental Organizations (NGOs) operate multiple community development initiatives simultaneously across diverse geographic locations, including clean water provision, mobile medical clinics, tribal school electrification, and women artisan vocational training. Tracking milestone velocity, budget consumption, beneficiary impact, and identifying lagging initiatives before deadlines pass is critical for donor accountability, regulatory transparency, and field team alignment.

The **NGO Project Progress Dashboard** is a lightweight, reliable, high-integrity web application paired with a robust end-to-end DevOps CI/CD and Configuration Management pipeline.

---

## 2. Problem Statement
Many NGOs still manage multi-million dollar donor programs via fragmented spreadsheets, email threads, and informal chat messages. This causes:
- **Delayed Risk Detection:** Bottlenecks and overdue milestones remain unnoticed until project deadlines pass.
- **Financial Discrepancy:** Disconnect between allocated budgets and actual field expenditures.
- **Fragmented Impact Data:** Lack of centralized real-time metrics on citizen beneficiaries reached.
- **DevOps Deficit:** Traditional academic/organizational software deployments rely on manual FTP uploads or SSH copy, leading to configuration drift, deployment failures, and zero automated rollback capabilities.

---

## 3. Project Stakeholders
| Stakeholder | Role / Perspective | Primary Objectives |
| :--- | :--- | :--- |
| **Executive Director / Donors** | Governance & Funding | High-level portfolio visibility, budget burn rate, impact metrics, audit trail. |
| **Field Project Leads** | Ground Execution | Milestone tracking, status reporting, logging field events, raising blockers. |
| **DevOps & QA Engineers** | Operations & Reliability | Automated continuous integration, fast feedback, automated Selenium regression testing, zero-downtime containerized deployments, idempotent server provisioning. |
| **Community Beneficiaries** | Impact Recipients | Direct beneficiaries of timely, accountable, well-funded social services. |

---

## 4. Key Objectives
1. **Real-time Portfolio Visibility:** Centralize all active NGO programs with dynamic KPI cards, progress bars, and budget utilization rates.
2. **Early Risk Warning System:** Automatically compute overdue status and flag at-risk initiatives (< 50% completion with under 30 days remaining).
3. **Automated Continuous Integration (CI):** Every commit triggers an automated build, lint check, unit tests, and Selenium browser tests.
4. **Containerized Portability (Docker):** Standardize deployment packages using multi-stage Dockerfiles across development, staging, and production.
5. **Configuration Management (Ansible):** Ensure immutable, idempotent infrastructure provisioning with automated rollback capabilities.

---

## 5. Constraints
- **Resource Footprint:** Must run smoothly on standard workstations and small cloud VMs (1-2 vCPU, 2GB-4GB RAM).
- **Zero-Setup Database:** Utilize embedded in-memory/file H2 database to eliminate complex external DBMS setup while maintaining full JPA/SQL compatibility.
- **Fast Pipeline Execution:** Total build, test, and containerization cycle must complete in under 5 minutes for rapid developer feedback.
- **Technology Constraints:** Pure Java 17/21, Spring Boot 3, Thymeleaf, standard CSS/Bootstrap, Jenkins, Docker, and Ansible.

---

## 6. MVP Scope (Minimum Viable Product)
- **Phase 1 (Core App):**
  - Project registration and editing form with input validation.
  - Interactive project detail view with milestone event checklist and progress recalculation.
  - Executive dashboard with 6 KPI metrics, real-time search, and alert banners.
  - Dedicated risk monitoring page (`/alerts`).
  - System health check REST endpoints (`/health`, `/actuator/health`).
- **Phase 2 (DevOps Pipeline):**
  - Declarative multi-stage Jenkinsfile pipeline.
  - Headless Chrome Selenium automated tests with failure screenshot hooks.
  - Multi-stage Docker image packaging and Docker Compose orchestration.
  - Ansible provisioning and automated rollback playbooks.
