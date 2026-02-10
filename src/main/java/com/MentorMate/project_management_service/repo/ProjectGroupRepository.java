package com.MentorMate.project_management_service.repo;

import com.MentorMate.project_management_service.entity.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectGroupRepository extends JpaRepository<ProjectGroup,Long> {
}
