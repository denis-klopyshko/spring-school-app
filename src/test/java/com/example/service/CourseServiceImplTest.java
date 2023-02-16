package com.example.service;

import com.example.dto.CourseDto;
import com.example.entity.Course;
import com.example.exception.ConflictException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.CourseRepository;
import com.example.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {
    @MockBean
    CourseRepository courseRepo;

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void shouldCreateNewCourse() {
        Course course = getCourseEntity();

        when(courseRepo.findByName(course.getName())).thenReturn(Optional.empty());
        when(courseRepo.save(any(Course.class))).thenReturn(course);

        CourseDto newCourseDto = CourseDto.builder().name("Math").description("Math Description").build();
        CourseDto courseDto = courseService.create(newCourseDto);

        assertThat(courseDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newCourseDto);

        assertThat(courseDto.getId()).isNotNull();

        verify(courseRepo).save(any(Course.class));
    }

    @Test
    void shouldNotCreateCourseAlreadyExists() {
        Course courseEntity = getCourseEntity();

        when(courseRepo.findByName(anyString())).thenReturn(Optional.of(courseEntity));

        assertThatThrownBy(() -> courseService.create(CourseDto.builder().name("Math").build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Course with name '%s' already exists!", courseEntity.getName());
    }

    @Test
    void shouldNotUpdateCourseNotFound() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.update(1L, new CourseDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course with id: 1 not found!");
    }

    @Test
    void shouldUpdateCourseNameAndDescription() {
        Course courseBeforeUpdate = getCourseEntity();
        Course courseAfterUpdate = getCourseEntity();
        courseAfterUpdate.setName("Biology");
        courseAfterUpdate.setDescription("Biology Description");

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(courseBeforeUpdate));
        when(courseRepo.save(any(Course.class))).thenReturn(courseAfterUpdate);

        CourseDto courseDto = courseService.update(1L, CourseDto.builder().id(1L).name("Biology").build());
        assertThat(courseDto.getName()).isEqualTo("Biology");
        assertThat(courseDto.getDescription()).isEqualTo("Biology Description");
    }

    @Test
    void shouldFailToDelete_CourseNotFound() {
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course with id: 1 not found!");
    }


    @Test
    void shouldDeleteCourse() {
        Course course = getCourseEntity();
        when(courseRepo.findById(course.getId())).thenReturn(Optional.of(course));

        courseService.delete(course.getId());

        verify(courseRepo).deleteById(course.getId());
    }

    private Course getCourseEntity() {
        return Course.builder()
                .id(1L)
                .name("Math")
                .description("Math Description")
                .build();
    }
}
