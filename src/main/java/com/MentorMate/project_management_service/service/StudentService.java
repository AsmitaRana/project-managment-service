package com.MentorMate.project_management_service.service;

import com.MentorMate.project_management_service.entity.*;
import com.MentorMate.project_management_service.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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


    public ProjectGroup createGroup(String name, Long leaderId) {

        Student leader = studentRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("Leader not found"));

        ProjectGroup group = new ProjectGroup();
        group.setName(name);
        group.setLeader(leader);

        return projectGroupRepository.save(group);
    }


    public ProjectGroup addMember(Long groupId, Long studentId, Long leaderId) {
        // 1️⃣ Fetch group
        ProjectGroup group = projectGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // 2️⃣ Authorization check
        if (!group.getLeader().getId().equals(leaderId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized");
        }

        // 3️⃣ Fetch student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 4️⃣ Check if student is already a member (active)
        boolean alreadyMember = projectMemberRepository
                .findByProjectGroupId(groupId)
                .stream()
                .anyMatch(m ->
                        m.getStudent() != null &&
                                studentId.equals(m.getStudent().getId()) &&
                                m.getRemovedAt() == null
                );

        if (alreadyMember) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student already member");
        }

        // 5️⃣ Create new ProjectMember entry
        ProjectMember newMember = new ProjectMember();
        newMember.setProjectGroup(group);
        newMember.setStudent(student);
        newMember.setAddedAt(java.time.LocalDateTime.now());

        projectMemberRepository.save(newMember);

        // 6️⃣ Also add student to group's members list (so response shows updated members)
        group.getMembers().add(student);

        // 7️⃣ Save group
        return projectGroupRepository.save(group);
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