package com.MentorMate.project_management_service.repo;

import com.MentorMate.project_management_service.entity.ProgressUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate,Long> {
    List<ProgressUpdate> findByProjectId(Long projectId);
}
