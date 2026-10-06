package org.ngo.dashboard.controller;

import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final ProjectService projectService;

    public DashboardController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(@RequestParam(value = "search", required = false) String search, Model model) {
        Map<String, Object> metrics = projectService.getDashboardMetrics();
        List<Project> projects = (search != null && !search.trim().isEmpty())
                ? projectService.searchProjects(search)
                : projectService.getAllProjects();

        List<Project> alertProjects = projectService.getAlertProjects();

        model.addAttribute("metrics", metrics);
        model.addAttribute("projects", projects);
        model.addAttribute("alertProjects", alertProjects);
        model.addAttribute("searchQuery", search != null ? search : "");
        model.addAttribute("activeNav", "dashboard");

        return "dashboard";
    }

    @GetMapping("/alerts")
    public String alertsView(Model model) {
        List<Project> alertProjects = projectService.getAlertProjects();
        List<Project> overdueProjects = projectService.getOverdueProjects();

        model.addAttribute("alertProjects", alertProjects);
        model.addAttribute("overdueProjects", overdueProjects);
        model.addAttribute("activeNav", "alerts");

        return "alerts";
    }
}
