# User Stories & Acceptance Criteria

This document details 10 core user stories with explicit acceptance criteria for the NGO Project Progress Dashboard.

---

### User Story 1: Executive Portfolio Overview
- **As an** NGO Director / Program Manager,
- **I want** to see an executive dashboard summarizing total initiatives, completed, in-progress, and delayed projects,
- **So that** I can assess our social impact portfolio at a single glance.
- **Acceptance Criteria:**
  - [x] Dashboard displays KPI cards: Total Projects, Completed, In Progress, Delayed, Total Beneficiaries, Total Budget, and Total Spent.
  - [x] Delivery percentage rate is calculated and displayed dynamically.
  - [x] Projects table lists all initiatives with progress bars and status badges.

---

### User Story 2: Project Data Registration
- **As a** Project Coordinator,
- **I want** to register a new NGO initiative with target beneficiaries, budget, location, and timeline,
- **So that** the organization has an official record of the planned project.
- **Acceptance Criteria:**
  - [x] Form provides inputs for Name, Description, Location, Beneficiaries, Budget, Spent, Status, Priority, Progress %, Officer, Start Date, and Deadline.
  - [x] Form enforces server-side validation (e.g., positive budget, valid dates, required names).
  - [x] Upon valid submission, the user is redirected to the projects directory with a success notification.

---

### User Story 3: Real-Time Initiative Search & Filter
- **As an** Operations Auditor,
- **I want** to search and filter initiatives by title, location, or lead officer,
- **So that** I can rapidly locate specific field programs without manual scrolling.
- **Acceptance Criteria:**
  - [x] Search input box filters projects in both dashboard and project listing views.
  - [x] Dropdown filter allows filtering by status (`PLANNED`, `IN_PROGRESS`, `COMPLETED`, `DELAYED`, `ON_HOLD`).
  - [x] If no matches exist, a friendly "No projects found" message is rendered.

---

### User Story 4: Milestone & Event Progress Tracking
- **As a** Field Project Lead,
- **I want** to log milestones for my project and toggle their completion status,
- **So that** team progress is documented accurately as work finishes on the ground.
- **Acceptance Criteria:**
  - [x] Project details page lists all associated milestones with target due dates.
  - [x] Field leads can add a new milestone directly on the project details page.
  - [x] Clicking a milestone toggle updates its completion state and recalculates the project's completion percentage.

---

### User Story 5: Early Warning Risk Alerts
- **As an** NGO Operations Director,
- **I want** to be alerted when projects are overdue or have low completion velocity near their target deadline,
- **So that** management can intervene and reallocate resources before projects fail.
- **Acceptance Criteria:**
  - [x] Any project whose deadline has passed and is not marked `COMPLETED` is flagged with an `OVERDUE` badge.
  - [x] Any project with less than 50% completion and under 30 days remaining is flagged in the risk warning banner.
  - [x] Dedicated `/alerts` page lists all high-risk initiatives with calculated risk factors.

---

### User Story 6: Automated Continuous Integration
- **As a** DevOps Engineer,
- **I want** every code push and pull request to trigger an automated Maven build, unit test execution, and packaging,
- **So that** defective code or broken contracts never reach staging or production.
- **Acceptance Criteria:**
  - [x] Jenkinsfile checkout, compiles Java code, and runs all JUnit tests.
  - [x] Build fails immediately if any test fails, blocking downstream artifact packaging.
  - [x] Successful builds archive the executable `.jar`/`.war` artifact.

---

### User Story 7: Automated Browser UI Regression Testing
- **As a** QA Automation Engineer,
- **I want** automated Selenium tests to verify dashboard rendering, search, and data entry in headless Chrome,
- **So that** UI regressions and broken navigation are caught before deployment.
- **Acceptance Criteria:**
  - [x] Selenium test suite executes against the running application in headless mode.
  - [x] If a test fails, a full-page screenshot is captured and saved to the `screenshots/` directory for triage.
  - [x] Test report is published to the Jenkins pipeline summary.

---

### User Story 8: Standardized Container Deployment
- **As a** Cloud Systems Administrator,
- **I want** the application packaged as a minimal, secure Docker container image,
- **So that** it runs identically across local development, CI test runners, and production servers.
- **Acceptance Criteria:**
  - [x] Multi-stage `Dockerfile` compiles the code in a Maven image and copies the artifact to a minimal JRE 21 runtime.
  - [x] Image exposes port 8080 and defines a container health check.
  - [x] Container can be started, stopped, restarted, and inspected via Docker CLI and `docker-compose`.

---

### User Story 9: Idempotent Configuration Management
- **As an** Infrastructure Engineer,
- **I want** an Ansible playbook that configures server prerequisites and launches the containerized application,
- **So that** server state is consistent and re-running the playbook yields `changed=0` (idempotency).
- **Acceptance Criteria:**
  - [x] Ansible playbook defines tasks for prerequisites, directories, Docker image pulling, and container execution.
  - [x] Consecutive executions demonstrate idempotency without unintended state modification.
  - [x] Playbook performs an automated HTTP health check (`curl http://localhost:8080/health`) before declaring success.

---

### User Story 10: Automated Rollback on Failure
- **As an** Incident Responder,
- **I want** an automated rollback mechanism to revert to the previous stable release if a new deployment is unhealthy,
- **So that** mean time to recovery (MTTR) is minimized and service uptime is preserved.
- **Acceptance Criteria:**
  - [x] An automated rollback script/playbook stops any failing container and restarts the previously tagged stable image.
  - [x] Post-rollback health check confirms the restored service is operational.
