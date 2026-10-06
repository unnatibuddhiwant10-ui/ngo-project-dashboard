# Git Branching Strategy & Workflow Guidelines

## 1. Branch Hierarchy

The repository adopts a disciplined Gitflow model tailored for continuous integration:

```
main (Production releases, tagged v1.0.0, protected)
 ▲
 │ Pull Request (Release cut & tested)
develop (Integration branch, nightlies, automated CI)
 ▲
 ├── feature/data-entry (Project registration, validation, milestones)
 ├── feature/dashboard (Search, KPI calculations, risk alerts)
 └── hotfix/dashboard-summary (Urgent patch & merge conflict resolution)
```

### Branch Roles
1. **`main` (Protected):**
   - Contains production-ready code only.
   - Direct pushes forbidden. Requires Pull Request with green CI pipeline.
   - Tagged with semantic versioning tags (e.g., `v1.0.0`, `v1.1.0`).
2. **`develop`:**
   - Serves as the continuous integration hub.
   - All feature branches branch off `develop` and merge back into `develop` via PR.
   - Triggers Jenkins continuous integration, unit tests, and headless Selenium tests.
3. **`feature/*`:**
   - Dedicated branches for discrete user stories (e.g., `feature/data-entry`, `feature/dashboard`).
   - Short-lived, regularly rebased or merged with `develop` to prevent merge divergence.
4. **`hotfix/*`:**
   - Rapid fixes applied directly to address critical issues or merge discrepancies.

---

## 2. Commit Message Conventions
Commits strictly follow the Conventional Commits specification:
- `feat: <description>` - A new feature for the user or dashboard
- `fix: <description>` - A bug fix
- `docs: <description>` - Documentation changes only
- `test: <description>` - Adding missing tests or correcting existing tests
- `ci: <description>` - Changes to CI/CD configuration files and scripts (Jenkins, Docker, Ansible)
- `refactor: <description>` - Code change that neither fixes a bug nor adds a feature

---

## 3. Deliberate Merge Conflict Exercise (Week 6)
To satisfy the DevOps evaluation criteria:
1. `feature/dashboard` and `hotfix/dashboard-summary` modify the dashboard metric aggregation method concurrently.
2. A merge attempt triggers `CONFLICT (content): Merge conflict in DashboardController.java`.
3. The conflict is manually inspected, reconciled to retain both high-priority alert counting and optimized caching, resolved, committed, and merged into `main` with release tag `v1.0.0`.
