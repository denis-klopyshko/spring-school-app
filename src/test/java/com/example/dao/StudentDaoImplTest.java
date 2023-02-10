package com.example.dao;

import com.example.dao.impl.StudentDaoImpl;
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

class StudentDaoImplTest extends BaseJpaDaoTest {
    
    @Autowired
    private StudentDaoImpl studentDao;
    
    @Test
    void shouldSaveNewStudentWithoutGroup() {
        Student savedStudent = studentDao.create(new Student("John", "Snow"));
        assertNotNull(savedStudent.getId());
    }

    @Test
    void shouldSaveNewStudentWithGroup() {
        Student savedStudent = studentDao.create(new Student(Group.ofId(100L), "firstname", "lastname"));
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
        Student savedStudent = studentDao.create(student);
        assertNotNull(savedStudent.getId());
        assertNotNull(savedStudent.getGroup());
        assertFalse(savedStudent.getCourses().isEmpty());
    }

    @Test
    void shouldFindStudentById() {
        Student expected = new Student(100L, Group.ofId(100L), "John", "Snow");
        Optional<Student> actual = studentDao.findById(100L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }


    @Test
    void shouldFindByFirstNameAndLastName() {
        Optional<Student> actual = studentDao.findByFirstNameAndLastName("John", "Snow");
        assertTrue(actual.isPresent());
        assertEquals("John Snow", actual.get().getFirstName() + " " + actual.get().getLastName());
    }

    @Test
    void shouldNotFindByFirstNameAndLastName() {
        Optional<Student> actual = studentDao.findByFirstNameAndLastName("John", "Napkins");
        assertFalse(actual.isPresent());
    }

    @Test
    void shouldFindStudentsByCourseName() {
        List<Student> expected = List.of(
                new Student(100L, Group.builder().id(100L).name("GR-10").build(), "John", "Snow")
        );
        List<Student> actual = studentDao.findAllByCourseName("Math");
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindStudentsByGroupId() {
        List<Student> expected = List.of(
                new Student(101L, Group.builder().id(101L).name("GR-11").build(), "Bob", "Rogers")
        );
        List<Student> actual = studentDao.findAllByGroupId(101L);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindById() {
        Optional<Student> studentFromDb = studentDao.findById(999L);
        assertFalse(studentFromDb.isPresent());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(studentDao.findById(101L).isPresent());
        studentDao.deleteById(101L);
        assertTrue(studentDao.findById(101L).isEmpty());
    }

    @Test
    void shouldUpdate() {
        Student expectedBeforeUpdate = new Student(101L, Group.ofId(101L), "John", "Snow");
        assertEquals(expectedBeforeUpdate, studentDao.findById(101L).get());

        Student expectedAfterUpdate = new Student(101L, Group.ofId(101L), "Irvin", "Hopkins");
        studentDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, studentDao.findById(101L).get());
    }

    @Test
    void shouldUpdateStudentGroup() {
        Student expectedBeforeUpdate = new Student(101L, Group.ofId(101L), "John", "Snow");
        assertEquals(expectedBeforeUpdate, studentDao.findById(101L).get());

        Student expectedAfterUpdate = new Student(101L, Group.ofId(102L), "John", "Snow");
        studentDao.update(expectedAfterUpdate);
        assertEquals(expectedAfterUpdate, studentDao.findById(101L).get());
    }

    @Test
    void shouldFindAll() {
        List<Student> expected = Arrays.asList(
                new Student(100L, Group.builder().id(100L).name("GR-10").build(), "John", "Snow"),
                new Student(101L, Group.builder().id(101L).name("GR-11").build(), "Bob", "Rogers"),
                new Student(102L, Group.builder().id(102L).name("GR-12").build(), "Roger", "That"),
                new Student(103L, null, "Irvin", "Napkins")
        );
        List<Student> actual = studentDao.findAll();
        assertEquals(expected, actual);
    }
}
