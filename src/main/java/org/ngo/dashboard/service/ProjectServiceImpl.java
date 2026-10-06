package org.ngo.dashboard.service;

import org.ngo.dashboard.model.Milestone;
import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.model.ProjectPriority;
import org.ngo.dashboard.model.ProjectStatus;
import org.ngo.dashboard.repository.MilestoneRepository;
import org.ngo.dashboard.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MilestoneRepository milestoneRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, MilestoneRepository milestoneRepository) {
        this.projectRepository = projectRepository;
        this.milestoneRepository = milestoneRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public Project saveProject(Project project) {
        // Auto-update status to COMPLETED if progress is 100%
        if (project.getProgressPercent() != null && project.getProgressPercent() == 100) {
            project.setStatus(ProjectStatus.COMPLETED);
        } else if (project.isOverdue() && project.getStatus() != ProjectStatus.COMPLETED) {
            project.setStatus(ProjectStatus.DELAYED);
        }
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> searchProjects(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProjects();
        }
        return projectRepository.searchProjects(query.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getOverdueProjects() {
        return projectRepository.findOverdueProjects(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getAlertProjects() {
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusDays(30);
        return projectRepository.findAlertProjects(today, cutoff);
    }

    @Override
    public Milestone addMilestone(Long projectId, Milestone milestone) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        project.addMilestone(milestone);
        return milestoneRepository.save(milestone);
    }

    @Override
    public void toggleMilestone(Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new IllegalArgumentException("Milestone not found with id: " + milestoneId));
        milestone.setAchieved(!milestone.isAchieved());
        milestoneRepository.save(milestone);

        // Recalculate project progress based on milestones if milestones exist
        Project project = milestone.getProject();
        if (project != null && project.getMilestones() != null && !project.getMilestones().isEmpty()) {
            long completed = project.getMilestones().stream().filter(Milestone::isAchieved).count();
            int calcProgress = (int) Math.round(((double) completed / project.getMilestones().size()) * 100);
            project.setProgressPercent(calcProgress);
            saveProject(project);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        long totalProjects = projectRepository.count();
        long completedProjects = projectRepository.countByStatus(ProjectStatus.COMPLETED);
        long inProgressProjects = projectRepository.countByStatus(ProjectStatus.IN_PROGRESS);
        long delayedProjects = projectRepository.countByStatus(ProjectStatus.DELAYED);
        long plannedProjects = projectRepository.countByStatus(ProjectStatus.PLANNED);
        long onHoldProjects = projectRepository.countByStatus(ProjectStatus.ON_HOLD);

        List<Project> alertProjects = getAlertProjects();
        Double totalBudget = projectRepository.sumTotalBudget();
        Double totalSpent = projectRepository.sumTotalSpent();
        Long totalBeneficiaries = projectRepository.sumTotalBeneficiaries();

        metrics.put("totalProjects", totalProjects);
        metrics.put("completedProjects", completedProjects);
        metrics.put("inProgressProjects", inProgressProjects);
        metrics.put("delayedProjects", delayedProjects);
        metrics.put("plannedProjects", plannedProjects);
        metrics.put("onHoldProjects", onHoldProjects);
        metrics.put("alertCount", alertProjects.size());
        metrics.put("totalBudget", totalBudget != null ? totalBudget : 0.0);
        metrics.put("totalSpent", totalSpent != null ? totalSpent : 0.0);
        metrics.put("totalBeneficiaries", totalBeneficiaries != null ? totalBeneficiaries : 0L);

        double completionRate = totalProjects > 0 ? ((double) completedProjects / totalProjects) * 100.0 : 0.0;
        metrics.put("completionRate", Math.round(completionRate * 10.0) / 10.0);

        return metrics;
    }

    @Override
    public void clearAllProjects() {
        milestoneRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Override
    public void resetSampleProjects() {
        clearAllProjects();
        LocalDate today = LocalDate.now();

        Project p1 = new Project(
                "Project Asha Deep: Child Nutrition & Warm Meals",
                "Combating acute infant malnutrition in flood-isolated riverine hamlets. Providing daily hot protein-dense khichdi, fortified milk, and weekly growth monitoring to 850 children and expectant mothers.",
                "Majuli River Island, Assam",
                850,
                18500.0,
                11800.0,
                ProjectStatus.IN_PROGRESS,
                ProjectPriority.HIGH,
                65,
                "Sister Mary Kurian (Grassroots Health Worker)",
                today.minusMonths(3),
                today.plusMonths(2)
        );
        p1.addMilestone(new Milestone("Village Elder & ASHA Mid-wives Townhall", "Securing community trust and screening 850 children", today.minusMonths(2), true, p1));
        p1.addMilestone(new Milestone("Decentralized Kitchens Setup", "Setting up 4 clean village cooking shelters with clean cookstoves", today.minusMonths(1), true, p1));
        p1.addMilestone(new Milestone("Monsoon Grain & Clean Water Supply", "Pre-positioning clean water drums and organic grain sacks", today.plusWeeks(3), false, p1));
        p1.addMilestone(new Milestone("Community Mother Support Handover", "Training 20 village mothers as certified nutrition champions", today.plusMonths(2), false, p1));
        projectRepository.save(p1);

        Project p2 = new Project(
                "Project Sanjeevani: Mobile Neo-Natal & Maternal Care",
                "Bringing emergency prenatal ultrasounds, hemoglobin testing, and maternal health kits to remote forest villages with zero road connectivity.",
                "Melghat Forest Belt, Maharashtra",
                3200,
                34000.0,
                33600.0,
                ProjectStatus.COMPLETED,
                ProjectPriority.MEDIUM,
                100,
                "Dr. Meera Nambiar (Volunteer Pediatrician)",
                today.minusMonths(6),
                today.minusWeeks(2)
        );
        p2.addMilestone(new Milestone("Outfitting 4x4 Camper as Mobile Clinic", "Installed solar battery backup, mini ultrasound, and blood centrifuge", today.minusMonths(5), true, p2));
        p2.addMilestone(new Milestone("Tribal Doula & Nurse Onboarding", "Empaneled 8 local tribal healthcare assistants", today.minusMonths(4), true, p2));
        p2.addMilestone(new Milestone("Completion of 75 Remote Forest Camps", "Conducted safe deliveries and prenatal care for over 3,200 women", today.minusWeeks(2), true, p2));
        projectRepository.save(p2);

        Project p3 = new Project(
                "Project Ujala: Solar Study Lanterns for Tribal Girls",
                "Distributing 600 durable solar study lanterns and organizing evening study circles so young tribal girls can complete high school homework safely without kerosene fume hazards.",
                "Palamu Forest Border, Jharkhand",
                600,
                14200.0,
                7900.0,
                ProjectStatus.DELAYED,
                ProjectPriority.CRITICAL,
                28,
                "Rajesh Soren (Community Organizer)",
                today.minusMonths(4),
                today.minusWeeks(1)
        );
        p3.addMilestone(new Milestone("Enrollment of 600 First-Generation Schoolgirls", "Door-to-door survey across 14 forest hamlets", today.minusMonths(3), true, p3));
        p3.addMilestone(new Milestone("Solar Lantern Freight Transit", "Shipment halted when mountain access bridge collapsed in monsoon", today.minusMonths(1), false, p3));
        p3.addMilestone(new Milestone("Evening Study Circle Volunteer Teacher Roster", "Enlisting 15 college student volunteers for evening tutoring", today.minusWeeks(1), false, p3));
        projectRepository.save(p3);

        Project p4 = new Project(
                "Project Saheli: Women's Honey & Handloom Collective",
                "Organizing marginalized women and tiger-widows into a self-reliant cooperative producing ethical wild mangrove honey and handloom cotton textiles.",
                "Sundarbans Delta Fringe, West Bengal",
                420,
                16000.0,
                6100.0,
                ProjectStatus.IN_PROGRESS,
                ProjectPriority.HIGH,
                38,
                "Fatima Bibi (Self-Help Group President)",
                today.minusMonths(2),
                today.plusDays(19)
        );
        p4.addMilestone(new Milestone("Cooperative Registration & Bank Linkage", "Bank account opened for 420 self-help members", today.minusMonths(1), true, p4));
        p4.addMilestone(new Milestone("Protective Bee-keeping Suits & Filtration Vats", "Procuring puncture-proof safety suits for forest collection", today.minusWeeks(1), false, p4));
        p4.addMilestone(new Milestone("Organic Certification & Local Farmers Market Launch", "First certified batch of 500kg honey for fair trade sale", today.plusDays(19), false, p4));
        projectRepository.save(p4);

        Project p5 = new Project(
                "Project Jal Jeevan: Arsenic-Free Deep Filtration Well",
                "Replacing shallow toxic arsenic hand-pumps with a 450-foot deep borewell equipped with community ceramic filters delivering 12,000 liters of safe drinking water daily.",
                "Ballia District, UP",
                5200,
                22000.0,
                3400.0,
                ProjectStatus.PLANNED,
                ProjectPriority.MEDIUM,
                15,
                "Gurpreet Singh (Rural Hydrologist)",
                today.minusWeeks(3),
                today.plusMonths(3)
        );
        p5.addMilestone(new Milestone("Water Quality Lab Testing & Site Selection", "Identified 3 arsenic-free aquifer spots", today.minusWeeks(1), true, p5));
        p5.addMilestone(new Milestone("Drilling & Geological Core Sampling", "Engaging drilling rig contractor", today.plusWeeks(3), false, p5));
        p5.addMilestone(new Milestone("Community Water User Committee Election", "Electing village youth for daily chlorination and valve maintenance", today.plusMonths(3), false, p5));
        projectRepository.save(p5);
    }
}
