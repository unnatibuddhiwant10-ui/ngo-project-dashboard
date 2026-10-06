# Automated Test Plan & Pipeline Quality Gate Specification

## 1. Test Strategy Overview
Quality assurance follows the standard testing pyramid:
1. **Unit & Data Access Tests:** Test isolated domain models, business logic in `ProjectServiceImpl`, and custom repository queries (`ProjectRepositoryTest`).
2. **Integration / MVC Tests:** Test web controllers and security/redirection flows via Spring MockMvc without full browser overhead.
3. **End-to-End Headless Browser Tests:** Test full rendered DOM, JavaScript, forms, and alerts using Selenium WebDriver 4 in Headless Chrome.

```
       / \
      /   \     End-to-End Selenium UI Tests (4 critical user journeys)
     /-----\
    /       \   MockMvc Integration Tests (5 HTTP endpoint tests)
   /---------\
  /           \ JUnit 5 Unit Tests (Business logic, calculations, metrics)
```

---

## 2. Selenium Test Cases Matrix

| Test ID | Method Name | Description | Assertion / Pass Criteria |
| :--- | :--- | :--- | :--- |
| **ST-01** | `testDashboardLoadsAndDisplaysMetrics` | Verifies dashboard renders navbar, KPI metric cards, and initiative table. | Total projects count &gt;= 1; elements present. |
| **ST-02** | `testCreateNewProjectAndVerifyListing` | Enters form data for a new initiative, submits, and checks table listing. | Redirects to `/projects`; new project name visible. |
| **ST-03** | `testSearchAndFilterProjects` | Inputs search term "Water", submits search form, checks filtered results. | Only matching project row displayed in table. |
| **ST-04** | `testAlertsAndDrillDown` | Navigates to `/alerts` view, inspects risk alerts table. | Overdue and low-progress projects flagged with warnings. |

---

## 3. Failure Screenshot Hook (`ScreenshotListener.java`)
- **Mechanism:** Implements JUnit 5 `TestWatcher` interface.
- **Trigger:** On `testFailed(ExtensionContext context, Throwable cause)`.
- **Action:** Grabs `TakesScreenshot` from `BaseSeleniumTest`, captures full PNG screenshot, and saves it into `screenshots/FAILED_<test-name>_<timestamp>.png`.
- **Purpose:** Enables rapid visual triage during automated headless CI execution without a GUI monitor.

---

## 4. Pipeline Quality Gating & Failure Simulation
DevOps evaluation requires demonstrating pipeline gating:
1. **Deliberate Bug Injection:** 
   - A deliberate defect is introduced (e.g. changing an expected page title or setting an invalid progress calculation threshold).
2. **Pipeline Failure & Deploy Block:**
   - Jenkins CI runs `mvn test`.
   - The test fails, triggers the failure screenshot hook, exits with error code 1.
   - Downstream stages (`Docker Build`, `Deploy to Tomcat`, `Ansible Provision`) are aborted immediately.
3. **Bug Fix & Green Rerun:**
   - Defect resolved in Git commit `fix: correct assertion threshold in test suite`.
   - Pipeline re-triggers and runs all stages green.
