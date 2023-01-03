package com.example.dao;

import com.example.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    List<Course> findAll();

    List<Course> findAllByStudentId(Long studentId);

    Optional<Course> findById(Long id);

    Course create(Course group);

    Course update(Course group);

    boolean deleteById(Long id);
}
