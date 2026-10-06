package org.ngo.dashboard.service;

import org.ngo.dashboard.model.Milestone;
import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.model.ProjectStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {

    List<Project> getAllProjects();

    Optional<Project> getProjectById(Long id);

    Project saveProject(Project project);

    void deleteProject(Long id);

    List<Project> searchProjects(String query);

    List<Project> getProjectsByStatus(ProjectStatus status);

    List<Project> getOverdueProjects();

    List<Project> getAlertProjects();

    Milestone addMilestone(Long projectId, Milestone milestone);

    void toggleMilestone(Long milestoneId);

    // Unified: High-performance KPI summary calculation with risk alert integration
    Map<String, Object> getDashboardMetrics();

    void clearAllProjects();

    void resetSampleProjects();
}
