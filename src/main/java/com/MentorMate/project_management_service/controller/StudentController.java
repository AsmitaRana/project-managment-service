    package com.MentorMate.project_management_service.controller;

    import com.MentorMate.project_management_service.dto.AddMemberRequest;
    import com.MentorMate.project_management_service.dto.RemoveMemberRequest;
    import com.MentorMate.project_management_service.entity.Document;
    import com.MentorMate.project_management_service.entity.Project;
    import com.MentorMate.project_management_service.entity.ProjectGroup;
    import com.MentorMate.project_management_service.entity.Student;
    import com.MentorMate.project_management_service.repo.StudentRepository;
    import com.MentorMate.project_management_service.service.StudentService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;

    @RestController
    @RequestMapping("/api/students")
    public class StudentController {

        @Autowired
        private StudentService studentService;

        @Autowired
        private StudentRepository studentRepository;

        @PostMapping("/create-student")
        public Student createStudent(@RequestBody Student student) {
            return studentRepository.save(student);
        }

        @PostMapping("/create-group")
        public ResponseEntity<ProjectGroup> createProjectGroup(
                @RequestParam String name,
                @RequestParam Long leaderId) {

            ProjectGroup group = studentService.createGroup(name, leaderId);

            return ResponseEntity.ok(group);
        }



        @PostMapping("/add-member")
        public ResponseEntity<ProjectGroup> addMember(
                @RequestBody AddMemberRequest request) {

            ProjectGroup group = studentService.addMember(
                    request.getGroupId(),
                    request.getStudentId(),
                    request.getLeaderId());

            return ResponseEntity.ok(group);
        }





        @DeleteMapping("/remove-member")
        public ResponseEntity<ProjectGroup> removeMember(@RequestBody RemoveMemberRequest request) {
            ProjectGroup group = studentService.removeMember(
                    request.getGroupId(),
                    request.getStudentId(),
                    request.getLeaderId()
            );
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
        public ResponseEntity<Document> uploadDocument(
                @RequestParam Long projectId,
                @RequestParam String name,
                @RequestParam Document.DocumentType type,
                @RequestParam("file") MultipartFile file,
                @RequestParam Long studentId) throws IOException {

            // ✅ Directory where files will be uploaded
            String uploadDir = "uploads";

            // ✅ Convert to Path
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

            // ✅ Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ✅ Full path to the uploaded file
            Path filePath = uploadPath.resolve(file.getOriginalFilename());

            // ✅ Transfer file to the path
            file.transferTo(filePath.toFile());

            // ✅ Call your service method to save Document info in DB
            Document doc = studentService.uploadDocument(projectId, name, type, filePath.toString(), studentId);

            return ResponseEntity.ok(doc);
        }


        @GetMapping("/project/{id}")
        public ResponseEntity<Project> viewProjectStatus(@PathVariable Long id, @RequestParam Long studentId) {
            Project project = studentService.viewProjectStatus(id, studentId);
            return ResponseEntity.ok(project);
        }
    }
