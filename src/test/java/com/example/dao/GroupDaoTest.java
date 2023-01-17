package com.example.dao;

import com.example.entity.Group;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoTest extends BaseDaoTest {
    @Test
    void shouldSaveNewGroup() {
        Group savedGroup = groupDao.create(new Group(1L, "GR-01"));
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
    void shouldNotFindById() {
        Optional<Group> groupFromDb = groupDao.findById(999L);
        assertFalse(groupFromDb.isPresent());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(groupDao.findById(103L).isPresent());
        groupDao.deleteById(103L);
        assertTrue(courseDao.findById(103L).isEmpty());
    }

    @Test
    void shouldNotDeleteByIdThrowsException() {
        assertTrue(groupDao.findById(101L).isPresent());
        assertThrows(DataIntegrityViolationException.class, () -> groupDao.deleteById(101L));
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
}
