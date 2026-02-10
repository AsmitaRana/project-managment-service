package com.MentorMate.project_management_service.repo;

import com.MentorMate.project_management_service.entity.Project;
import com.MentorMate.project_management_service.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    List<Project> findByGuideId(Long guideId);
    List<ProjectMember> findByProjectGroupId(Long groupId);
}

