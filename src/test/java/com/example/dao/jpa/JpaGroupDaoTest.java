package com.example.dao.jpa;

import com.example.entity.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JpaGroupDaoTest extends BaseJpaDaoTest {
    @Autowired
    private JpaGroupDao jpaGroupDao;

    @Test
    void shouldSaveNewGroup() {
        Group savedGroup = jpaGroupDao.create(new Group("GR-01"));
        assertNotNull(savedGroup.getId());
    }

    @Test
    void shouldFindGroupById() {
        Group expected = new Group(100L, "GR-10");
        Optional<Group> actual = jpaGroupDao.findById(100L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void shouldFindGroupByName() {
        Optional<Group> actual = jpaGroupDao.findByName("GR-12");
        assertTrue(actual.isPresent());
        assertEquals("GR-12", actual.get().getName());
    }

    @Test
    void shouldNotFindGroupByName() {
        Optional<Group> actual = jpaGroupDao.findByName("GR-18");
        assertFalse(actual.isPresent());
    }

    @Test
    void shouldNotFindById() {
        Optional<Group> groupFromDb = jpaGroupDao.findById(999L);
        assertFalse(groupFromDb.isPresent());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(jpaGroupDao.findById(103L).isPresent());
        jpaGroupDao.deleteById(103L);
        assertTrue(jpaGroupDao.findById(103L).isEmpty());
    }

    @Test
    void shouldUpdate() {
        Group expectedBeforeUpdate = new Group(101L, "GR-11");
        assertEquals(expectedBeforeUpdate, jpaGroupDao.findById(101L).get());

        Group expectedAfterUpdate = new Group(101L, "GR-14");
        jpaGroupDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, jpaGroupDao.findById(101L).get());
    }

    @Test
    void shouldFindAll() {
        List<Group> expected = Arrays.asList(
                new Group(100L, "GR-10"),
                new Group(101L, "GR-11"),
                new Group(102L, "GR-12"),
                new Group(103L, "GR-13")
        );
        List<Group> actual = jpaGroupDao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllByStudentsLimit() {
        List<Group> expected = List.of(
                new Group(103L, "GR-13")
        );
        List<Group> actual = jpaGroupDao.findAllWithLessOrEqualStudents(0L);
        assertEquals(expected, actual);
    }
}
