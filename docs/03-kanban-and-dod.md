# Kanban Board & Definition of Done (DoD)

## 1. GitHub Projects Kanban Board Layout

The project follows an agile Kanban methodology mapped across 5 workflow columns:

```
+----------------+  +----------------+  +----------------+  +----------------+  +----------------+
|    BACKLOG     |  |  IN PROGRESS   |  |   CODE REVIEW  |  |    TEST / QA   |  |      DONE      |
|  (User Stories |  | (Feature Branch|  |  (PR Open, CI  |  | (Selenium Suite|  | (Merged Main,  |
|  & New Tasks)  |  |   Active)      |  |   Checks Pass) |  |   Gated Stage) |  |  Deployed Tag) |
+----------------+  +----------------+  +----------------+  +----------------+  +----------------+
```

### Column Criteria & Work-in-Progress (WIP) Limits
| Column | Purpose | Exit Criteria | WIP Limit |
| :--- | :--- | :--- | :--- |
| **1. Backlog** | Prioritized user stories, technical debt, and pipeline enhancements. | Item has clear description, acceptance criteria, and priority tag. | Unlimited |
| **2. In Progress** | Stories actively being coded on dedicated feature branches (`feature/*`). | Branch created from `develop`; local unit tests passing. | 3 per dev |
| **3. Code Review** | Pull requests submitted for peer review. Automated CI pipeline triggered. | At least one peer review comment; Jenkins CI build succeeds. | 2 |
| **4. Test / QA** | Deployed to staging environment; automated Selenium regression tests executed. | All automated UI tests pass; zero screenshot failures. | 2 |
| **5. Done** | Feature merged to `main`, tagged with release version, deployed via Ansible. | Meets complete Definition of Done (DoD) below. | Unlimited |

---

## 2. Definition of Done (DoD)
A user story or feature task is strictly considered **"DONE"** only when all of the following conditions are verified:

1. **Code Completeness:**
   - [x] Implements all functional requirements specified in the user story acceptance criteria.
   - [x] Code adheres to clean coding standards (meaningful naming, clean MVC separation, proper error handling).
2. **Automated Testing:**
   - [x] Unit tests written for services and repositories with > 80% code coverage.
   - [x] MockMvc integration tests verify web controllers and HTTP response codes.
   - [x] Selenium automated end-to-end browser tests pass in headless mode.
3. **CI/CD Quality Gates:**
   - [x] Jenkins declarative pipeline runs green (`Checkout` &rarr; `Build` &rarr; `Test` &rarr; `Package`).
   - [x] No compilation errors, lint failures, or broken test assertions.
4. **Containerization & Deployment:**
   - [x] Application successfully packages into Docker image using multi-stage `Dockerfile`.
   - [x] Container passes automated health check (`/health` returns HTTP 200 `UP`).
   - [x] Ansible playbook runs idempotently (`changed=0` on repeated run).
5. **Version Control & Documentation:**
   - [x] Git commits follow conventional commit messages (`feat:`, `fix:`, `docs:`, `ci:`).
   - [x] Branch merged via Pull Request into `develop` / `main`.
   - [x] Documentation updated in `docs/` and evidence screenshots saved.
