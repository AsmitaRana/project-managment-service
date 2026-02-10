package com.MentorMate.project_management_service.service;

import com.MentorMate.project_management_service.entity.Guide;
import com.MentorMate.project_management_service.entity.ProgressUpdate;
import com.MentorMate.project_management_service.entity.Project;
import com.MentorMate.project_management_service.entity.ProjectStatusEntity;
import com.MentorMate.project_management_service.repo.GuideRepository;
import com.MentorMate.project_management_service.repo.ProgressUpdateRepository;
import com.MentorMate.project_management_service.repo.ProjectRepository;
import com.MentorMate.project_management_service.repo.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectStatusRepository projectStatusRepository;

    @Autowired
    private ProgressUpdateRepository progressUpdateRepository;

    public List<Project> viewAssignedProjects(Long guideId) {
        return projectRepository.findByGuideId(guideId);
    }

    public Project approveOrRejectProject(Long projectId, Project.ProjectStatus status, String remarks, Long guideId) {
        // Fetch project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Authorization check
        if (!project.getGuide().getId().equals(guideId))
            throw new RuntimeException("Not authorized");

        // Update project status
        project.setStatus(status);
        if (status == Project.ProjectStatus.APPROVED) {
            project.setApprovedAt(java.time.LocalDateTime.now());
        }
        projectRepository.save(project);

        // Create project status record
        ProjectStatusEntity statusEntity = new ProjectStatusEntity();
        statusEntity.setProject(project);
        statusEntity.setStatus(status);

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new RuntimeException("Guide not found"));
        statusEntity.setChangedBy(guide);
        statusEntity.setRemarks(remarks);

        // ✅ Save using repository
        projectStatusRepository.save(statusEntity);

        return project;
    }


    public ProgressUpdate updateProjectProgress(Long projectId, String update, Long guideId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        if (!project.getGuide().getId().equals(guideId)) throw new RuntimeException("Not authorized");
        ProgressUpdate progress = new ProgressUpdate();
        progress.setProject(project);
        progress.setUpdateText(update);
        Guide guide = guideRepository.findById(guideId).orElseThrow(() -> new RuntimeException("Guide not found"));
        progress.setUpdatedBy(guide);
        return progressUpdateRepository.save(progress);
    }

    public ProgressUpdate addRemarks(Long projectId, String remarks, Long guideId) {
        return updateProjectProgress(projectId, remarks, guideId);
    }

}
