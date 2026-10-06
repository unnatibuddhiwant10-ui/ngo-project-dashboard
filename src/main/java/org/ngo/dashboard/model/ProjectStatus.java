package org.ngo.dashboard.model;

public enum ProjectStatus {
    PLANNED("Planned", "badge-secondary"),
    IN_PROGRESS("In Progress", "badge-primary"),
    COMPLETED("Completed", "badge-success"),
    DELAYED("Delayed", "badge-danger"),
    ON_HOLD("On Hold", "badge-warning");

    private final String displayName;
    private final String badgeClass;

    ProjectStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
