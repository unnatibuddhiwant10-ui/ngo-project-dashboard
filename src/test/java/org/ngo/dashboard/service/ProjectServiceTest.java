package org.ngo.dashboard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ngo.dashboard.model.Milestone;
import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.model.ProjectPriority;
import org.ngo.dashboard.model.ProjectStatus;
import org.ngo.dashboard.repository.MilestoneRepository;
import org.ngo.dashboard.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project(
                "Test Rural Healthcare Camp",
                "Providing essential health diagnostics to 500 villagers.",
                "Sonbhadra, UP",
                500,
                15000.0,
                3000.0,
                ProjectStatus.IN_PROGRESS,
                ProjectPriority.HIGH,
                40,
                "Dr. Amit Verma",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(2)
        );
    }

    @Test
    @DisplayName("Should successfully save and retrieve a project")
    void testSaveAndGetProject() {
        Project saved = projectService.saveProject(testProject);
        assertNotNull(saved.getId(), "Saved project must have an auto-generated ID");

        Optional<Project> retrieved = projectService.getProjectById(saved.getId());
        assertTrue(retrieved.isPresent(), "Project should be retrievable from repository");
        assertEquals("Test Rural Healthcare Camp", retrieved.get().getName());
        assertEquals("Dr. Amit Verma", retrieved.get().getLeadOfficer());
    }

    @Test
    @DisplayName("Should automatically mark project as COMPLETED when progress reaches 100%")
    void testAutoCompleteOnFullProgress() {
        testProject.setProgressPercent(100);
        Project saved = projectService.saveProject(testProject);
        assertEquals(ProjectStatus.COMPLETED, saved.getStatus(), "Status must automatically transition to COMPLETED");
    }

    @Test
    @DisplayName("Should search projects by name, location, or lead officer")
    void testSearchProjects() {
        projectService.saveProject(testProject);

        List<Project> searchByName = projectService.searchProjects("Healthcare");
        assertFalse(searchByName.isEmpty(), "Should find project by substring in name");

        List<Project> searchByLocation = projectService.searchProjects("Sonbhadra");
        assertFalse(searchByLocation.isEmpty(), "Should find project by location");

        List<Project> searchByOfficer = projectService.searchProjects("Amit");
        assertFalse(searchByOfficer.isEmpty(), "Should find project by officer name");
    }

    @Test
    @DisplayName("Should calculate dashboard KPI metrics correctly")
    void testDashboardMetricsCalculation() {
        projectService.saveProject(testProject);

        Map<String, Object> metrics = projectService.getDashboardMetrics();
        assertNotNull(metrics);
        assertTrue((Long) metrics.get("totalProjects") >= 1, "Total projects must be at least 1");
        assertNotNull(metrics.get("totalBudget"));
        assertNotNull(metrics.get("totalSpent"));
        assertNotNull(metrics.get("completionRate"));
    }

    @Test
    @DisplayName("Should add milestone and toggle completion state")
    void testAddAndToggleMilestone() {
        Project savedProject = projectService.saveProject(testProject);

        Milestone milestone = new Milestone(
                "Initial Community Survey",
                "Surveying 100 households",
                LocalDate.now().plusDays(10),
                false,
                savedProject
        );

        Milestone savedMilestone = projectService.addMilestone(savedProject.getId(), milestone);
        assertNotNull(savedMilestone.getId(), "Milestone should have generated ID");
        assertFalse(savedMilestone.isAchieved(), "Milestone initially should be pending");

        projectService.toggleMilestone(savedMilestone.getId());
        Milestone updated = milestoneRepository.findById(savedMilestone.getId()).orElseThrow();
        assertTrue(updated.isAchieved(), "Milestone should be toggled to achieved");
    }
}
