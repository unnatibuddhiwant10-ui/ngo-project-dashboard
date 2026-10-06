package org.ngo.dashboard.config;

import org.ngo.dashboard.model.Milestone;
import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.model.ProjectPriority;
import org.ngo.dashboard.model.ProjectStatus;
import org.ngo.dashboard.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataSeeder {

    @Value("${app.seed.data:true}")
    private boolean seedDataEnabled;

    @Bean
    CommandLineRunner seedDatabase(ProjectRepository projectRepository) {
        return args -> {
            if (!seedDataEnabled || projectRepository.count() > 0) {
                return; // Seed data disabled or already populated
            }

            LocalDate today = LocalDate.now();

            // Project 1: Asha Deep - Child Nutrition & Warm Meals
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

            // Project 2: Sanjeevani - Mobile Neo-Natal & Maternal Care Van
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

            // Project 3: Ujala - Solar Lanterns for Tribal Girls (Delayed Alert - Monsoon road wash-out)
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
                    today.minusWeeks(1) // Overdue deadline due to washed-out culvert!
            );
            p3.addMilestone(new Milestone("Enrollment of 600 First-Generation Schoolgirls", "Door-to-door survey across 14 forest hamlets", today.minusMonths(3), true, p3));
            p3.addMilestone(new Milestone("Solar Lantern Freight Transit", "Shipment halted when mountain access bridge collapsed in monsoon", today.minusMonths(1), false, p3));
            p3.addMilestone(new Milestone("Evening Study Circle Volunteer Teacher Roster", "Enlisting 15 college student volunteers for evening tutoring", today.minusWeeks(1), false, p3));
            projectRepository.save(p3);

            // Project 4: Saheli - Women Forest Honey & Handloom Collective (Low Progress Warning)
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
                    today.plusDays(19) // Due in 19 days, progress 38% - needs field support
            );
            p4.addMilestone(new Milestone("Cooperative Registration & Bank Linkage", "Bank account opened for 420 self-help members", today.minusMonths(1), true, p4));
            p4.addMilestone(new Milestone("Protective Bee-keeping Suits & Filtration Vats", "Procuring puncture-proof safety suits for forest collection", today.minusWeeks(1), false, p4));
            p4.addMilestone(new Milestone("Organic Certification & Local Farmers Market Launch", "First certified batch of 500kg honey for fair trade sale", today.plusDays(19), false, p4));
            projectRepository.save(p4);

            // Project 5: Jal Jeevan - Clean Arsenic-Free Community Well
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
        };
    }
}
