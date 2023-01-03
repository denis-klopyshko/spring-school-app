package com.example.dao;

import com.example.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {
    List<Student> findAll();

    Optional<Student> findById(Long id);

    Student create(Student group);

    Student update(Student group);

    boolean deleteById(Long id);
}
