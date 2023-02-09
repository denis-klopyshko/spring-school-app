package com.example.dao.jpa;

import com.example.entity.Course;
import com.example.entity.Group;
import com.example.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaStudentDaoTest extends BaseJpaDaoTest {
    
    @Autowired
    private JpaStudentDao jpaStudentDao;
    
    @Test
    void shouldSaveNewStudentWithoutGroup() {
        Student savedStudent = jpaStudentDao.create(new Student("John", "Snow"));
        assertNotNull(savedStudent.getId());
    }

    @Test
    void shouldSaveNewStudentWithGroup() {
        Student savedStudent = jpaStudentDao.create(new Student(Group.ofId(100L), "firstname", "lastname"));
        assertNotNull(savedStudent.getId());
        assertNotNull(savedStudent.getGroup());
    }

    @Test
    void shouldSaveNewStudentWithGroupAndAssignedCourses() {
        Student student = Student.builder()
                .group(Group.ofId(100L))
                .firstName("firstname")
                .lastName("lastname")
                .courses(List.of(Course.ofId((100L))))
                .build();
        Student savedStudent = jpaStudentDao.create(student);
        assertNotNull(savedStudent.getId());
        assertNotNull(savedStudent.getGroup());
        assertFalse(savedStudent.getCourses().isEmpty());
    }

    @Test
    void shouldFindStudentById() {
        Student expected = new Student(100L, Group.ofId(100L), "John", "Snow");
        Optional<Student> actual = jpaStudentDao.findById(100L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void shouldNotFindById() {
        Optional<Student> studentFromDb = jpaStudentDao.findById(999L);
        assertFalse(studentFromDb.isPresent());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(jpaStudentDao.findById(101L).isPresent());
        jpaStudentDao.deleteById(101L);
        assertTrue(jpaStudentDao.findById(101L).isEmpty());
    }

    @Test
    void shouldUpdate() {
        Student expectedBeforeUpdate = new Student(101L, Group.ofId(101L), "John", "Snow");
        assertEquals(expectedBeforeUpdate, jpaStudentDao.findById(101L).get());

        Student expectedAfterUpdate = new Student(101L, Group.ofId(101L), "Irvin", "Hopkins");
        jpaStudentDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, jpaStudentDao.findById(101L).get());
    }

    @Test
    void shouldUpdateStudentGroup() {
        Student expectedBeforeUpdate = new Student(101L, Group.ofId(101L), "John", "Snow");
        assertEquals(expectedBeforeUpdate, jpaStudentDao.findById(101L).get());

        Student expectedAfterUpdate = new Student(101L, Group.ofId(102L), "John", "Snow");
        jpaStudentDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, jpaStudentDao.findById(101L).get());
    }

    @Test
    void shouldFindAll() {
        List<Student> expected = Arrays.asList(
                new Student(100L, Group.builder().id(100L).name("GR-10").build(), "John", "Snow"),
                new Student(101L, Group.builder().id(101L).name("GR-11").build(), "Bob", "Rogers"),
                new Student(102L, Group.builder().id(102L).name("GR-12").build(), "Roger", "That")
        );
        List<Student> actual = jpaStudentDao.findAll();
        assertEquals(expected, actual);
    }
}
