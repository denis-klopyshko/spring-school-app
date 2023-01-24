package com.example.dao;

import com.example.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {
    List<Student> findAll();

    List<Student> findAllByGroupId(Long groupId);

    Optional<Student> findById(Long id);

    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);

    Student create(Student student);

    Student update(Student student);

    boolean deleteById(Long id);

    boolean assignStudentOnCourse(Long studentId, Long courseId);

    void removeStudentCourses(Long studentId);

    Long count();
}
