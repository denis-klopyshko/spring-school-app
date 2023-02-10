package com.example.dao;

import com.example.dao.impl.GroupDaoImpl;
import com.example.entity.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoImplTest extends BaseJpaDaoTest {
    @Autowired
    private GroupDaoImpl groupDao;

    @Test
    void shouldSaveNewGroup() {
        Group savedGroup = groupDao.create(new Group("GR-01"));
        assertNotNull(savedGroup.getId());
    }

    @Test
    void shouldFindGroupById() {
        Group expected = new Group(100L, "GR-10");
        Optional<Group> actual = groupDao.findById(100L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void shouldFindGroupByName() {
        Optional<Group> actual = groupDao.findByName("GR-12");
        assertTrue(actual.isPresent());
        assertEquals("GR-12", actual.get().getName());
    }

    @Test
    void shouldNotFindGroupByName() {
        Optional<Group> actual = groupDao.findByName("GR-18");
        assertFalse(actual.isPresent());
    }

    @Test
    void shouldNotFindById() {
        Optional<Group> groupFromDb = groupDao.findById(999L);
        assertFalse(groupFromDb.isPresent());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(groupDao.findById(103L).isPresent());
        groupDao.deleteById(103L);
        assertTrue(groupDao.findById(103L).isEmpty());
    }

    @Test
    void shouldUpdate() {
        Group expectedBeforeUpdate = new Group(101L, "GR-11");
        assertEquals(expectedBeforeUpdate, groupDao.findById(101L).get());

        Group expectedAfterUpdate = new Group(101L, "GR-14");
        groupDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, groupDao.findById(101L).get());
    }

    @Test
    void shouldFindAll() {
        List<Group> expected = Arrays.asList(
                new Group(100L, "GR-10"),
                new Group(101L, "GR-11"),
                new Group(102L, "GR-12"),
                new Group(103L, "GR-13")
        );
        List<Group> actual = groupDao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllByStudentsLimit() {
        List<Group> expected = List.of(
                new Group(103L, "GR-13")
        );
        List<Group> actual = groupDao.findAllWithLessOrEqualStudents(0L);
        assertEquals(expected, actual);
    }
}
