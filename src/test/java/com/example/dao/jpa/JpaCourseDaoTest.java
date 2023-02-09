package com.example.dao.jpa;


import com.example.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaCourseDaoTest extends BaseJpaDaoTest {
    @Autowired
    private JpaCourseDao jpaCourseDao;

    @Test
    void shouldSaveNewCourse() {
        Course savedCourse = jpaCourseDao.create(new Course("math", "description"));
        assertNotNull(savedCourse.getId());
    }

    @Test
    void shouldFindCourseById() {
        Course expected = new Course(100L, "Math", "Math Description");
        Optional<Course> actual = jpaCourseDao.findById(100L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void shouldNotFindById() {
        Optional<Course> courseFromDb = jpaCourseDao.findById(999L);
        assertFalse(courseFromDb.isPresent());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(jpaCourseDao.findById(101L).isPresent());
        jpaCourseDao.deleteById(101L);
        assertTrue(jpaCourseDao.findById(101L).isEmpty());
    }

    @Test
    void shouldFindByName() {
        Optional<Course> course = jpaCourseDao.findByName("Math");
        assertTrue(course.isPresent());
    }

    @Test
    void shouldReturnTotalNumberOfEntities() {
        assertEquals(3, jpaCourseDao.count());
    }

    @Test
    void shouldUpdate() {
        Course expectedBeforeUpdate = new Course(101L, "Biology", "Biology Description");
        assertEquals(expectedBeforeUpdate, jpaCourseDao.findById(101L).get());

        Course expectedAfterUpdate = new Course(101L, "I", "Changed");
        jpaCourseDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, jpaCourseDao.findById(101L).get());
    }

    @Test
    void shouldFindAll() {
        List<Course> expected = List.of(
                new Course(100L, "Math", "Math Description"),
                new Course(101L, "Biology", "Biology Description"),
                new Course(102L, "Programming", "Programming Description")
        );
        List<Course> actual = jpaCourseDao.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllByStudentId() {
        List<Course> expected = List.of(
                new Course(100L, "Math", "Math Description"),
                new Course(101L, "Biology", "Biology Description")
        );
        List<Course> actual = jpaCourseDao.findAllByStudentId(100L);
        assertEquals(expected, actual);
    }
}
