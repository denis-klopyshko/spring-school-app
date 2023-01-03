package com.example.dao;

import com.example.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    List<Group> findAll();

    Optional<Group> findById(Long id);

    Group create(Group group);

    Group update(Group group);

    boolean deleteById(Long id);
}
