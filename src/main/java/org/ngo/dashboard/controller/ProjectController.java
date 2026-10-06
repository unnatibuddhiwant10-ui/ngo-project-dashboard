package org.ngo.dashboard.controller;

import jakarta.validation.Valid;
import org.ngo.dashboard.model.Milestone;
import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.model.ProjectPriority;
import org.ngo.dashboard.model.ProjectStatus;
import org.ngo.dashboard.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String listProjects(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "status", required = false) ProjectStatus status,
                               Model model) {
        List<Project> projects;
        if (search != null && !search.trim().isEmpty()) {
            projects = projectService.searchProjects(search);
        } else if (status != null) {
            projects = projectService.getProjectsByStatus(status);
        } else {
            projects = projectService.getAllProjects();
        }

        model.addAttribute("projects", projects);
        model.addAttribute("searchQuery", search != null ? search : "");
        model.addAttribute("selectedStatus", status);
        model.addAttribute("allStatuses", ProjectStatus.values());
        model.addAttribute("activeNav", "projects");
        return "project-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Project project = new Project();
        project.setStartDate(LocalDate.now());
        project.setTargetEndDate(LocalDate.now().plusMonths(3));
        project.setProgressPercent(0);
        project.setSpent(0.0);

        model.addAttribute("project", project);
        model.addAttribute("allStatuses", ProjectStatus.values());
        model.addAttribute("allPriorities", ProjectPriority.values());
        model.addAttribute("activeNav", "new-project");
        return "project-form";
    }

    @PostMapping("/save")
    public String saveProject(@Valid @ModelAttribute("project") Project project,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allStatuses", ProjectStatus.values());
            model.addAttribute("allPriorities", ProjectPriority.values());
            model.addAttribute("activeNav", "new-project");
            return "project-form";
        }

        projectService.saveProject(project);
        redirectAttributes.addFlashAttribute("successMessage", "Project '" + project.getName() + "' saved successfully!");
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String showProjectDetail(@PathVariable("id") Long id, Model model) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id: " + id));

        model.addAttribute("project", project);
        model.addAttribute("newMilestone", new Milestone());
        model.addAttribute("activeNav", "projects");
        return "project-detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id: " + id));

        model.addAttribute("project", project);
        model.addAttribute("allStatuses", ProjectStatus.values());
        model.addAttribute("allPriorities", ProjectPriority.values());
        model.addAttribute("activeNav", "projects");
        return "project-form";
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteProject(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully.");
        return "redirect:/projects";
    }

    @PostMapping("/{id}/milestones")
    public String addMilestone(@PathVariable("id") Long id,
                               @ModelAttribute("newMilestone") Milestone milestone,
                               RedirectAttributes redirectAttributes) {
        if (milestone.getTitle() != null && !milestone.getTitle().trim().isEmpty()) {
            if (milestone.getDueDate() == null) {
                milestone.setDueDate(LocalDate.now().plusWeeks(2));
            }
            projectService.addMilestone(id, milestone);
            redirectAttributes.addFlashAttribute("successMessage", "Milestone added successfully!");
        }
        return "redirect:/projects/" + id;
    }

    @PostMapping("/{projectId}/milestones/{milestoneId}/toggle")
    public String toggleMilestone(@PathVariable("projectId") Long projectId,
                                  @PathVariable("milestoneId") Long milestoneId) {
        projectService.toggleMilestone(milestoneId);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/clear-all")
    public String clearAllProjects(RedirectAttributes redirectAttributes) {
        projectService.clearAllProjects();
        redirectAttributes.addFlashAttribute("successMessage", "Default data removed! Workspace is now fresh and ready for your real NGO data.");
        return "redirect:/dashboard";
    }

    @PostMapping("/reset-sample")
    public String resetSampleProjects(RedirectAttributes redirectAttributes) {
        projectService.resetSampleProjects();
        redirectAttributes.addFlashAttribute("successMessage", "Humanitarian community initiatives loaded successfully!");
        return "redirect:/dashboard";
    }
}
