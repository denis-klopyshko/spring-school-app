package com.example.mapping;

import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.example.entity.Course;
import com.example.entity.Group;
import com.example.entity.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudentDtoMapperTest {
    private static final StudentMapper MAPPER = StudentMapper.INSTANCE;

    @Test
    void shouldMapEntityToDto() {
        Group group = Group.builder().id(1L).build();
        Course course = Course.builder().id(1L).name("Math").description("Description").build();
        Student studentEntity = Student.builder()
                .id(1L)
                .group(group)
                .firstName("John")
                .lastName("Snow")
                .courses(List.of(course))
                .build();

        StudentDto studentDto = MAPPER.mapToDto(studentEntity);

        assertThat(studentDto)
                .usingRecursiveComparison()
                .isEqualTo(studentEntity);
    }

    @Test
    void shouldMapDtoToEntity() {
        GroupDto groupDto = GroupDto.builder().id(1L).build();
        CourseDto courseDto = CourseDto.builder().id(1L).name("Math").description("Description").build();
        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .group(groupDto)
                .firstName("John")
                .lastName("Snow")
                .courses(List.of(courseDto))
                .build();

        Student studentEntity = MAPPER.mapToEntity(studentDto);

        assertThat(studentEntity)
                .usingRecursiveComparison()
                .isEqualTo(studentDto);
    }

    @Test
    void shouldMapBaseAttributesFromDtoToEntity() {
        GroupDto groupDto = GroupDto.builder().id(1L).build();
        CourseDto courseDto = CourseDto.builder().id(1L).name("Math").description("Description").build();
        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .group(groupDto)
                .firstName("John")
                .lastName("Snow")
                .courses(List.of(courseDto))
                .build();

        Student studentEntity = MAPPER.mapBaseAttributes(studentDto);

        assertThat(studentEntity.getGroup()).isNull();
        assertThat(studentEntity.getCourses()).isEmpty();

        assertThat(studentEntity.getId()).isNull();
        assertThat(studentEntity.getFirstName()).isEqualTo(studentDto.getFirstName());
        assertThat(studentEntity.getLastName()).isEqualTo(studentDto.getLastName());
    }

    @Test
    void shouldUpdateEntityWithDto() {
        CourseDto courseDto = CourseDto.builder().id(1L).name("Math").description("Description").build();
        List<CourseDto> courses = new ArrayList<>();
        courses.add(courseDto);
        StudentDto studentDto = StudentDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Snow")
                .courses(courses)
                .build();

        Student studentEntity = Student.builder()
                .id(1L)
                .group(Group.builder().id(2L).build())
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();


        MAPPER.updateStudentFromDto(studentDto, studentEntity);

        assertThat(studentEntity.getGroup()).isNull();
        assertThat(studentEntity.getCourses()).isEmpty();

        assertThat(studentEntity.getId()).isEqualTo(1L);
        assertThat(studentEntity.getFirstName()).isEqualTo(studentDto.getFirstName());
        assertThat(studentEntity.getLastName()).isEqualTo(studentDto.getLastName());
    }
}
