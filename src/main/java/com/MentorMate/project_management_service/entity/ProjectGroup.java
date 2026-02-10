package com.MentorMate.project_management_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

    @Entity
    @Table(name = "project_groups")
    @Data
    public class ProjectGroup {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @ManyToOne
        @JoinColumn(name = "leader_id", nullable = false)
        private Student leader;

        @ManyToMany
        @JoinTable(
                name = "project_members",
                joinColumns = @JoinColumn(name = "project_group_id"),
                inverseJoinColumns = @JoinColumn(name = "student_id")
        )
        private List<Student> members;

        @Column(nullable = false)
        private LocalDateTime createdAt = LocalDateTime.now();

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

        public Student getLeader() {
            return leader;
        }

        public void setLeader(Student leader) {
            this.leader = leader;
        }

        public List<Student> getMembers() {
            return members;
        }

        public void setMembers(List<Student> members) {
            this.members = members;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
