package org.ngo.dashboard.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardSeleniumTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Selenium Test 1: Verify Dashboard loads and renders all KPI metrics")
    void testDashboardLoadsAndDisplaysMetrics() {
        driver.get(getBaseUrl() + "/dashboard");

        // Assert Title & Brand
        assertTrue(driver.getTitle().contains("NGO Project"), "Page title should contain 'NGO Project'");
        WebElement brand = driver.findElement(By.className("navbar-brand"));
        assertTrue(brand.getText().contains("NGO Project Dashboard"));

        // Assert KPI Cards
        WebElement totalCard = driver.findElement(By.id("kpi-total-val"));
        assertNotNull(totalCard);
        int totalProjects = Integer.parseInt(totalCard.getText().trim());
        assertTrue(totalProjects >= 1, "There should be seeded projects in total count");

        WebElement inProgressCard = driver.findElement(By.id("kpi-inprogress-val"));
        assertNotNull(inProgressCard);

        // Assert table presence
        WebElement table = driver.findElement(By.id("projects-table"));
        assertNotNull(table);
    }

    @Test
    @DisplayName("Selenium Test 2: Add a new NGO initiative via form and verify in directory")
    void testCreateNewProjectAndVerifyListing() {
        driver.get(getBaseUrl() + "/projects/new");

        // Fill form
        driver.findElement(By.id("name")).sendKeys("Selenium Solar Lighting Drive");
        driver.findElement(By.id("description")).sendKeys("Installing 50 community solar streetlights in off-grid tribal hamlets.");
        driver.findElement(By.id("location")).sendKeys("Bastar, Chhattisgarh");
        driver.findElement(By.id("leadOfficer")).sendKeys("Kavita Rao");
        driver.findElement(By.id("beneficiaryCount")).sendKeys("2500");
        driver.findElement(By.id("budget")).sendKeys("22000.00");
        driver.findElement(By.id("spent")).sendKeys("4500.00");
        driver.findElement(By.id("progressPercent")).clear();
        driver.findElement(By.id("progressPercent")).sendKeys("20");

        // Submit form
        driver.findElement(By.id("save-project-btn")).click();

        // Verify redirection and project listing
        assertTrue(driver.getCurrentUrl().contains("/projects"));
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Selenium Solar Lighting Drive"), "Newly created project must appear in listing");
        assertTrue(pageSource.contains("Kavita Rao"), "Lead officer name must appear in listing");
    }

    @Test
    @DisplayName("Selenium Test 3: Search projects by keyword and verify filtered output")
    void testSearchAndFilterProjects() {
        driver.get(getBaseUrl() + "/dashboard");

        WebElement searchInput = driver.findElement(By.id("search-input"));
        searchInput.clear();
        searchInput.sendKeys("Water");

        driver.findElement(By.id("search-btn")).click();

        // Verify filtered table contains clean water initiative
        WebElement table = driver.findElement(By.id("projects-table"));
        String tableText = table.getText();
        assertTrue(tableText.contains("Clean Water & Sanitation Initiative"), "Search must return matching project");
    }

    @Test
    @DisplayName("Selenium Test 4: View risk alerts feed and confirm overdue initiative flagged")
    void testAlertsAndDrillDown() {
        driver.get(getBaseUrl() + "/alerts");

        assertTrue(driver.getTitle().contains("Risk Alerts"));
        WebElement alertsTable = driver.findElement(By.id("alerts-table"));
        assertNotNull(alertsTable);

        // Verify seeded overdue or delayed project is flagged
        String alertsText = alertsTable.getText();
        assertTrue(alertsText.contains("Solar Electrification for Tribal Schools") ||
                   alertsText.contains("Women Artisan Livelihood Training"),
                "Alerts view must list at-risk initiatives");
    }
}
