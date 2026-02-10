package com.MentorMate.project_management_service.repo;

import com.MentorMate.project_management_service.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByEmail(String email);
}
