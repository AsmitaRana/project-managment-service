package com.MentorMate.project_management_service;

import com.MentorMate.project_management_service.entity.Project;
import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String filePath;

    private Long studentId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    public enum DocumentType {
        REPORT,
        PPT,
        OTHER
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }
}
