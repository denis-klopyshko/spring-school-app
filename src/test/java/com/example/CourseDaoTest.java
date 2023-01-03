package com.example;

import com.example.entity.Course;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseDaoTest extends BaseDaoTest {
    @Test
    void shouldSaveNewCourse() {
        Course savedCourse = courseDao.create(new Course("math", "description"));
        assertNotNull(savedCourse.getId());
    }

    @Test
    void shouldFindCourseById() {
        Course expected = new Course(100L, "Math", "Math Description");
        Optional<Course> actual = courseDao.findById(100L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void shouldNotFindById() {
        Optional<Course> courseFromDb = courseDao.findById(999L);
        assertFalse(courseFromDb.isPresent());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(courseDao.findById(101L).isPresent());
        assertTrue(courseDao.deleteById(101L));
    }

    @Test
    void shouldNotDeleteById() {
        assertFalse(courseDao.deleteById(9999L));
    }

    @Test
    void shouldUpdate() {
        Course expectedBeforeUpdate = new Course(101L, "Biology", "Biology Description");
        assertEquals(expectedBeforeUpdate, courseDao.findById(101L).get());

        Course expectedAfterUpdate = new Course(101L, "I", "Changed");
        courseDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, courseDao.findById(101L).get());
    }

    @Test
    void shouldFindAll() {
        List<Course> expected = List.of(
                new Course(100L, "Math", "Math Description"),
                new Course(101L, "Biology", "Biology Description"),
                new Course(102L, "Programming", "Programming Description")
        );
        List<Course> actual = courseDao.findAll();

        assertEquals(expected, actual);
    }
}