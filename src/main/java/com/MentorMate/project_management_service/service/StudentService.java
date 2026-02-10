package com.MentorMate.project_management_service.service;

import com.MentorMate.project_management_service.entity.*;
import com.MentorMate.project_management_service.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProjectGroupRepository projectGroupRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private GuideRepository guideRepository;

    public ProjectGroup createProjectGroup(String name, Long leaderId) {
        Student leader = studentRepository.findById(leaderId).orElseThrow(() -> new RuntimeException("Student not found"));
        ProjectGroup group = new ProjectGroup();
        group.setName(name);
        group.setLeader(leader);
        return projectGroupRepository.save(group);
    }

    public ProjectGroup addMember(Long groupId, Long studentId, Long leaderId) {
        // Fetch group
        ProjectGroup group = projectGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Authorization check
        if (!group.getLeader().getId().equals(leaderId))
            throw new RuntimeException("Not authorized");

        // Fetch student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Add student to group members
        group.getMembers().add(student);

        // Optionally create ProjectMember record
        ProjectMember member = projectMemberRepository.findByProjectGroupId(groupId).stream()
                .filter(m -> m.getStudent() != null &&
                        studentId.equals(m.getStudent().getId()) &&
                        m.getRemovedAt() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // ✅ Save the ProjectGroup if needed
        projectGroupRepository.save(group);

        // ✅ Return the updated group
        return group;
    }


    public ProjectGroup removeMember(Long groupId, Long studentId, Long leaderId) {
        ProjectGroup group = projectGroupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        if (!group.getLeader().getId().equals(leaderId)) throw new RuntimeException("Not authorized");
        group.getMembers().removeIf(s -> s.getId().equals(studentId));
        ProjectMember member = projectMemberRepository.findByProjectGroupId(groupId).stream()
                .filter(m ->
                        m.getStudent() != null &&
                                studentId.equals(m.getStudent().getId()) &&
                                m.getRemovedAt() == null
                )

                .findFirst().orElseThrow(() -> new RuntimeException("Member not found"));
        member.setRemovedAt(java.time.LocalDateTime.now());
        projectMemberRepository.save(member);
        return projectGroupRepository.save(group);
    }

    public Project chooseGuide(Long projectId, Long guideId, Long studentId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectGroup group = project.getProjectGroup();
        if (!group.getLeader().getId().equals(studentId)) throw new RuntimeException("Not authorized");
        Guide guide = guideRepository.findById(guideId).orElseThrow(() -> new RuntimeException("Guide not found"));
        project.setGuide(guide);
        return projectRepository.save(project);
    }

    public Project submitProjectIdea(String title, String description, Long groupId, Long studentId) {
        ProjectGroup group = projectGroupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        if (!group.getLeader().getId().equals(studentId)) throw new RuntimeException("Not authorized");
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setProjectGroup(group);
        return projectRepository.save(project);
    }

    public Document uploadDocument(Long projectId, String name, Document.DocumentType type, String filePath, Long studentId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Document doc = new Document();
        doc.setProject(project);
        doc.setName(name);
        doc.setType(type);
        doc.setFilePath(filePath);
        doc.setUploadedBy(student);
        return documentRepository.save(doc);
    }

    public Project viewProjectStatus(Long projectId, Long studentId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectGroup group = project.getProjectGroup();
        if (!group.getLeader().getId().equals(studentId) && !group.getMembers().stream().anyMatch(s -> s.getId().equals(studentId))) {
            throw new RuntimeException("Not authorized");
        }
        return project;
    }
}