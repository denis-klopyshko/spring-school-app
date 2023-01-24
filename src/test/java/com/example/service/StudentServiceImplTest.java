package com.example.service;

import com.example.dao.CourseDao;
import com.example.dao.GroupDao;
import com.example.dao.StudentDao;
import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.example.entity.Course;
import com.example.entity.Group;
import com.example.entity.Student;
import com.example.exception.ResourceNotFoundException;
import com.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    CourseDao courseDao;

    @Mock
    GroupDao groupDao;

    @Mock
    StudentDao studentDao;

    @InjectMocks
    StudentServiceImpl studentService;

    @Test
    void shouldCreateNewStudent() {
        Student student = getStudentEntity();
        Course courseEntity = Course.builder().id(1L).name("Math").description("Math Description").build();
        StudentDto newStudentDto = getStudentDto();

        when(studentDao.create(any(Student.class))).thenReturn(student);
        when(groupDao.findById(anyLong())).thenReturn(Optional.of(new Group()));
        when(courseDao.findById(anyLong())).thenReturn(Optional.of(courseEntity));

        StudentDto studentDto = studentService.create(newStudentDto);

        assertThat(studentDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newStudentDto);

        assertThat(studentDto.getId()).isNotNull();
    }

    @Test
    void shouldDeleteStudent() {
        when(studentDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student with id: 1 not found!");

    }

    private Student getStudentEntity() {
        Group group = Group.ofId(1L);
        return Student.builder()
                .id(1L)
                .group(group)
                .firstName("John")
                .lastName("Snow")
                .build();
    }

    private StudentDto getStudentDto() {
        GroupDto groupDto = GroupDto.ofId(1L);
        CourseDto courseDto = CourseDto.builder().id(1L).name("Math").description("Math Description").build();
        return StudentDto.builder()
                .group(groupDto)
                .firstName("John")
                .lastName("Snow")
                .courses(List.of(courseDto))
                .build();
    }
}
