package com.example.dao;

import com.example.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    List<Course> findAll();

    List<Course> findAllByStudentId(Long studentId);

    Optional<Course> findById(Long id);

    Optional<Course> findByName(String name);

    Course create(Course course);

    Course update(Course course);

    void deleteById(Long id);

    Long count();
}
