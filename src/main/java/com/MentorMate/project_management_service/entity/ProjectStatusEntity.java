package com.MentorMate.project_management_service.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_status")
@Data
public class ProjectStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Project.ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "changed_by", nullable = false)
    private Guide changedBy;

    @Column(nullable = false)
    private LocalDateTime changedAt = LocalDateTime.now();

    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project.ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(Project.ProjectStatus status) {
        this.status = status;
    }

    public Guide getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Guide changedBy) {
        this.changedBy = changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
