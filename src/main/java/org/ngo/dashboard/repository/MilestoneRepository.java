package org.ngo.dashboard.repository;

import org.ngo.dashboard.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByProjectIdOrderByDueDateAsc(Long projectId);
    List<Milestone> findByAchievedFalseOrderByDueDateAsc();
}
