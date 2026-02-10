    package com.MentorMate.project_management_service.controller;

    import com.MentorMate.project_management_service.entity.Document;
    import com.MentorMate.project_management_service.entity.Project;
    import com.MentorMate.project_management_service.entity.ProjectGroup;
    import com.MentorMate.project_management_service.service.StudentService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.File;
    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;

    @RestController
    @RequestMapping("/api/students")
    public class StudentController {

        @Autowired
        private StudentService studentService;

        @PostMapping("/create-group")
        public ResponseEntity<ProjectGroup> createProjectGroup(@RequestParam String name, @RequestParam Long leaderId) {
            ProjectGroup group = studentService.createProjectGroup(name, leaderId);
            return ResponseEntity.ok(group);
        }


        @PostMapping("/add-member")
        public ResponseEntity<ProjectGroup> addMember(@RequestParam Long groupId, @RequestParam Long studentId, @RequestParam Long leaderId) {
            ProjectGroup group = studentService.addMember(groupId, studentId, leaderId);
            return ResponseEntity.ok(group);
        }

        @PostMapping("/remove-member")
        public ResponseEntity<ProjectGroup> removeMember(@RequestParam Long groupId, @RequestParam Long studentId, @RequestParam Long leaderId) {
            ProjectGroup group = studentService.removeMember(groupId, studentId, leaderId);
            return ResponseEntity.ok(group);
        }

        @PostMapping("/choose-guide")
        public ResponseEntity<Project> chooseGuide(@RequestParam Long projectId, @RequestParam Long guideId, @RequestParam Long studentId) {
            Project project = studentService.chooseGuide(projectId, guideId, studentId);
            return ResponseEntity.ok(project);
        }

        @PostMapping("/submit-project")
        public ResponseEntity<Project> submitProjectIdea(@RequestParam String title, @RequestParam String description, @RequestParam Long groupId, @RequestParam Long studentId) {
            Project project = studentService.submitProjectIdea(title, description, groupId, studentId);
            return ResponseEntity.ok(project);
        }

        @PostMapping("/upload-document")
        public ResponseEntity<Document> uploadDocument(@RequestParam Long projectId, @RequestParam String name, @RequestParam Document.DocumentType type, @RequestParam("file") MultipartFile file, @RequestParam Long studentId) throws IOException {
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            Document doc = studentService.uploadDocument(projectId, name, type, filePath, studentId);
            return ResponseEntity.ok(doc);
        }

        @GetMapping("/project/{id}")
        public ResponseEntity<Project> viewProjectStatus(@PathVariable Long id, @RequestParam Long studentId) {
            Project project = studentService.viewProjectStatus(id, studentId);
            return ResponseEntity.ok(project);
        }
    }
