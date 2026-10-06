package org.ngo.dashboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(min = 3, max = 150, message = "Project name must be between 3 and 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Location is required")
    @Column(nullable = false, length = 100)
    private String location;

    @NotNull(message = "Target beneficiary count is required")
    @Min(value = 1, message = "Beneficiary count must be at least 1")
    private Integer beneficiaryCount;

    @NotNull(message = "Allocated budget is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0")
    private Double budget;

    @NotNull(message = "Spent amount is required")
    @DecimalMin(value = "0.0", message = "Spent amount cannot be negative")
    private Double spent = 0.0;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectPriority priority = ProjectPriority.MEDIUM;

    @NotNull(message = "Progress percentage is required")
    @Min(value = 0, message = "Progress cannot be less than 0%")
    @Max(value = 100, message = "Progress cannot exceed 100%")
    private Integer progressPercent = 0;

    @NotBlank(message = "Lead officer name is required")
    @Column(nullable = false, length = 100)
    private String leadOfficer;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Target end date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetEndDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Milestone> milestones = new ArrayList<>();

    public Project() {
    }

    public Project(String name, String description, String location, Integer beneficiaryCount,
                   Double budget, Double spent, ProjectStatus status, ProjectPriority priority,
                   Integer progressPercent, String leadOfficer, LocalDate startDate, LocalDate targetEndDate) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.beneficiaryCount = beneficiaryCount;
        this.budget = budget;
        this.spent = spent;
        this.status = status;
        this.priority = priority;
        this.progressPercent = progressPercent;
        this.leadOfficer = leadOfficer;
        this.startDate = startDate;
        this.targetEndDate = targetEndDate;
    }

    // Helper methods for Dashboard & Alerts
    public boolean isOverdue() {
        if (status == ProjectStatus.COMPLETED) {
            return false;
        }
        return targetEndDate != null && targetEndDate.isBefore(LocalDate.now());
    }

    public boolean isLowProgressAlert() {
        if (status == ProjectStatus.COMPLETED) {
            return false;
        }
        if (isOverdue()) {
            return true;
        }
        // Alert if less than 30 days left and progress is below 50%
        long daysRemaining = getDaysRemaining();
        return daysRemaining <= 30 && progressPercent < 50;
    }

    public long getDaysRemaining() {
        if (targetEndDate == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), targetEndDate);
    }

    public double getBudgetUtilizationPercent() {
        if (budget == null || budget <= 0) return 0.0;
        return Math.min(100.0, Math.round((spent / budget) * 1000.0) / 10.0);
    }

    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
        milestone.setProject(this);
    }

    public void removeMilestone(Milestone milestone) {
        milestones.remove(milestone);
        milestone.setProject(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getBeneficiaryCount() {
        return beneficiaryCount;
    }

    public void setBeneficiaryCount(Integer beneficiaryCount) {
        this.beneficiaryCount = beneficiaryCount;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getSpent() {
        return spent;
    }

    public void setSpent(Double spent) {
        this.spent = spent;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public ProjectPriority getPriority() {
        return priority;
    }

    public void setPriority(ProjectPriority priority) {
        this.priority = priority;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getLeadOfficer() {
        return leadOfficer;
    }

    public void setLeadOfficer(String leadOfficer) {
        this.leadOfficer = leadOfficer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(LocalDate targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }
}
