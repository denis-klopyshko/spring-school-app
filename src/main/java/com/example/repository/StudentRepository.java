package com.example.repository;

import com.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByGroupId(Long groupId);

    @Query("select distinct s from Student s join fetch s.courses c where c.name = ?1")
    List<Student> findAllByCourseName(String courseName);
}
