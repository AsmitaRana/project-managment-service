package com.MentorMate.project_management_service.controller;

import com.MentorMate.project_management_service.entity.ProgressUpdate;
import com.MentorMate.project_management_service.entity.Project;
import com.MentorMate.project_management_service.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @GetMapping("/projects/{guideId}")
    public ResponseEntity<List<Project>> viewAssignedProjects(@PathVariable Long guideId) {
        List<Project> projects = guideService.viewAssignedProjects(guideId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/approve-reject")
    public ResponseEntity<Project> approveOrRejectProject(@RequestParam Long projectId, @RequestParam Project.ProjectStatus status, @RequestParam(required = false) String remarks, @RequestParam Long guideId) {
        Project project = guideService.approveOrRejectProject(projectId, status, remarks, guideId);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/update-progress")
    public ResponseEntity<ProgressUpdate> updateProjectProgress(@RequestParam Long projectId, @RequestParam String update, @RequestParam Long guideId) {
        ProgressUpdate progress = guideService.updateProjectProgress(projectId, update, guideId);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/add-remarks")
    public ResponseEntity<ProgressUpdate> addRemarks(@RequestParam Long projectId, @RequestParam String remarks, @RequestParam Long guideId) {
        ProgressUpdate progress = guideService.addRemarks(projectId, remarks, guideId);
        return ResponseEntity.ok(progress);
    }
}
