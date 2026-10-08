package org.ngo.dashboard.selenium;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DashboardSeleniumTest extends BaseSeleniumTest {

    // ============================================================
    // TEST 1: Dashboard loads and KPI metrics are displayed
    // ============================================================
    @Test
    @DisplayName("Selenium Test 1: Verify Dashboard loads and renders all KPI metrics")
    void testDashboardLoadsAndDisplaysMetrics() {

        driver.get(getBaseUrl() + "/dashboard");

        // Verify page title
        assertTrue(
                driver.getTitle().contains("NGO Project"),
                "Page title should contain 'NGO Project'"
        );

        // Verify navbar brand
        WebElement brand = driver.findElement(By.className("navbar-brand"));

        assertTrue(
                brand.getText().contains("NGO Project Dashboard"),
                "Navbar should display 'NGO Project Dashboard'"
        );

        // Verify total projects KPI
        WebElement totalCard =
                driver.findElement(By.id("kpi-total-val"));

        assertNotNull(
                totalCard,
                "Total projects KPI should be present"
        );

        int totalProjects =
                Integer.parseInt(totalCard.getText().trim());

        assertTrue(
                totalProjects >= 1,
                "There should be seeded projects in total count"
        );

        // Verify in-progress KPI
        WebElement inProgressCard =
                driver.findElement(By.id("kpi-inprogress-val"));

        assertNotNull(
                inProgressCard,
                "In-progress KPI should be present"
        );

        // Verify projects table
        WebElement table =
                driver.findElement(By.id("projects-table"));

        assertNotNull(
                table,
                "Projects table should be present"
        );
    }


    // ============================================================
    // TEST 2: Create a new project
    // ============================================================
    @Test
    @DisplayName("Selenium Test 2: Add a new NGO initiative via form and verify in directory")
    void testCreateNewProjectAndVerifyListing() {

        driver.get(getBaseUrl() + "/projects/new");

        // Fill project name
        driver.findElement(By.id("name"))
                .sendKeys("Selenium Solar Lighting Drive");

        // Fill description
        driver.findElement(By.id("description"))
                .sendKeys(
                        "Installing 50 community solar streetlights in off-grid tribal hamlets."
                );

        // Fill location
        driver.findElement(By.id("location"))
                .sendKeys("Bastar, Chhattisgarh");

        // Fill lead officer
        driver.findElement(By.id("leadOfficer"))
                .sendKeys("Kavita Rao");

        // Fill beneficiary count
        driver.findElement(By.id("beneficiaryCount"))
                .sendKeys("2500");

        // Fill budget
        driver.findElement(By.id("budget"))
                .sendKeys("22000.00");

        // Fill spent amount
        driver.findElement(By.id("spent"))
                .sendKeys("4500.00");

        // Fill progress percentage
        WebElement progressInput =
                driver.findElement(By.id("progressPercent"));

        progressInput.clear();
        progressInput.sendKeys("20");

        // Submit form
        driver.findElement(By.id("save-project-btn"))
                .click();

        // Verify redirected to projects page
        assertTrue(
                driver.getCurrentUrl().contains("/projects"),
                "After saving, user should be redirected to projects page"
        );

        // Verify newly created project appears
        String pageSource = driver.getPageSource();

        assertTrue(
                pageSource.contains("Selenium Solar Lighting Drive"),
                "Newly created project must appear in listing"
        );

        // Verify lead officer appears
        assertTrue(
                pageSource.contains("Kavita Rao"),
                "Lead officer name must appear in listing"
        );
    }


 // ============================================================
// TEST 3: Search project
// ============================================================
@Test
@DisplayName("Selenium Test 3: Search projects by keyword and verify filtered output")
void testSearchAndFilterProjects() {

    // Open dashboard
    driver.get(getBaseUrl() + "/dashboard");

    // Locate search input
    WebElement searchInput =
            driver.findElement(By.id("search-input"));

    // Enter search keyword
    searchInput.clear();
    searchInput.sendKeys("Jal");

    // Click search button
    driver.findElement(By.id("search-btn"))
            .click();

    // Wait briefly for the search results/table to refresh
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    // IMPORTANT:
    // Locate the table AFTER the search operation has completed.
    // This avoids StaleElementReferenceException.
    WebElement tableAfterSearch =
            driver.findElement(By.id("projects-table"));

    // Read the refreshed table content
    String tableText =
            tableAfterSearch.getText();

    // Verify that the searched keyword appears
    assertTrue(
            tableText.toLowerCase().contains("jal"),
            "Search results should contain the searched keyword 'Jal'"
    );

    // Verify the expected project appears
    assertTrue(
            tableText.contains("Project Jal Jeevan"),
            "Search results should contain the Jal Jeevan project"
    );
}


    // ============================================================
    // TEST 4: Risk alerts
    // ============================================================
    @Test
    @DisplayName("Selenium Test 4: View risk alerts feed and confirm at-risk initiative is displayed")
    void testAlertsAndDrillDown() {

        driver.get(getBaseUrl() + "/alerts");

        // Verify page title
        assertTrue(
                driver.getTitle().contains("Risk Alerts"),
                "Alerts page title should contain 'Risk Alerts'"
        );

        // Locate alerts table
        WebElement alertsTable =
                driver.findElement(By.id("alerts-table"));

        assertNotNull(
                alertsTable,
                "Alerts table should be present"
        );

        // Get alerts table text
        String alertsText =
                alertsTable.getText();

        // Verify actual seeded alert projects
        assertTrue(
                alertsText.contains(
                        "Project Ujala: Solar Study Lanterns for Tribal Girls"
                )
                ||
                alertsText.contains(
                        "Project Saheli: Women's Honey & Handloom Collective"
                ),
                "Alerts view must list at-risk initiatives"
        );
    }
}