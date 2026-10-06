package org.ngo.dashboard.controller;

import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiProjectController {

    private final ProjectService projectService;

    public ApiProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project saved = projectService.saveProject(project);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMetrics() {
        return projectService.getDashboardMetrics();
    }

    @GetMapping("/alerts")
    public List<Project> getAlerts() {
        return projectService.getAlertProjects();
    }
}
