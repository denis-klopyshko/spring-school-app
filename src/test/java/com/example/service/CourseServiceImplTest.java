package com.example.service;

import com.example.dao.CourseDao;
import com.example.dto.CourseDto;
import com.example.entity.Course;
import com.example.exception.ConflictException;
import com.example.exception.ResourceNotFoundException;
import com.example.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    @Mock
    CourseDao courseDao;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void shouldCreateNewCourse() {
        Course course = getCourseEntity();

        when(courseDao.findByName(course.getName())).thenReturn(Optional.empty());
        when(courseDao.create(any(Course.class))).thenReturn(course);

        CourseDto newCourseDto = CourseDto.builder().name("Math").description("Math Description").build();
        CourseDto courseDto = courseService.create(newCourseDto);

        assertThat(courseDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newCourseDto);

        assertThat(courseDto.getId()).isNotNull();

        verify(courseDao).create(any(Course.class));
    }

    @Test
    void shouldNotCreateCourseAlreadyExists() {
        Course courseEntity = getCourseEntity();

        when(courseDao.findByName(anyString())).thenReturn(Optional.of(courseEntity));

        assertThatThrownBy(() -> courseService.create(CourseDto.builder().name("Math").build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Course with name '%s' already exists!", courseEntity.getName());
    }

    @Test
    void shouldNotUpdateCourseNotFound() {
        when(courseDao.findById(anyLong())).thenReturn(Optional.empty());

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

        when(courseDao.findById(anyLong())).thenReturn(Optional.of(courseBeforeUpdate));
        when(courseDao.update(any(Course.class))).thenReturn(courseAfterUpdate);

        CourseDto courseDto = courseService.update(1L, CourseDto.builder().id(1L).name("Biology").build());
        assertThat(courseDto.getName()).isEqualTo("Biology");
        assertThat(courseDto.getDescription()).isEqualTo("Biology Description");
    }

    @Test
    void shouldFailToDelete_CourseNotFound() {
        when(courseDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course with id: 1 not found!");
    }


    @Test
    void shouldDeleteCourse() {
        Course course = getCourseEntity();
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseDao).deleteById(1L);

        courseService.delete(1L);

        verify(courseDao).deleteById(1L);
    }

    private Course getCourseEntity() {
        return Course.builder()
                .id(1L)
                .name("Math")
                .description("Math Description")
                .build();
    }
}
