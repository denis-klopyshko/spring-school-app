package com.example.repository;

import com.example.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);

    @Query("select g from Group g where size(g.students)<= ?1")
    List<Group> findAllByStudentsIsLessThanOrEqual(int studentLimit);
}
