package com.example.dao;

import com.example.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    List<Group> findAll();

    List<Group> findAllWithLessOrEqualStudents(Long studentsQuantity);

    Optional<Group> findById(Long id);

    Optional<Group> findByName(String name);

    Group create(Group group);

    Group update(Group group);

    void deleteById(Long id);

    Long count();
}
