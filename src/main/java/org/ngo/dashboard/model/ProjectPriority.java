package org.ngo.dashboard.model;

public enum ProjectPriority {
    LOW("Low", "priority-low"),
    MEDIUM("Medium", "priority-medium"),
    HIGH("High", "priority-high"),
    CRITICAL("Critical", "priority-critical");

    private final String displayName;
    private final String cssClass;

    ProjectPriority(String displayName, String cssClass) {
        this.displayName = displayName;
        this.cssClass = cssClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCssClass() {
        return cssClass;
    }
}
