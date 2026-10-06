package org.ngo.dashboard.repository;

import org.ngo.dashboard.model.Project;
import org.ngo.dashboard.model.ProjectPriority;
import org.ngo.dashboard.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByPriority(ProjectPriority priority);

    long countByStatus(ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.location) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.leadOfficer) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Project> searchProjects(@Param("query") String query);

    @Query("SELECT p FROM Project p WHERE p.targetEndDate < :today AND p.status != 'COMPLETED'")
    List<Project> findOverdueProjects(@Param("today") LocalDate today);

    @Query("SELECT p FROM Project p WHERE p.status != 'COMPLETED' AND " +
           "(p.targetEndDate < :today OR (p.targetEndDate <= :cutoffDate AND p.progressPercent < 50))")
    List<Project> findAlertProjects(@Param("today") LocalDate today, @Param("cutoffDate") LocalDate cutoffDate);

    @Query("SELECT SUM(p.budget) FROM Project p")
    Double sumTotalBudget();

    @Query("SELECT SUM(p.spent) FROM Project p")
    Double sumTotalSpent();

    @Query("SELECT SUM(p.beneficiaryCount) FROM Project p")
    Long sumTotalBeneficiaries();
}
