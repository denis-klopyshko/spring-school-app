package com.example.mapping;

import com.example.dto.CourseDto;
import com.example.entity.Course;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDtoMapperTest {
    private static final CourseMapper MAPPER = CourseMapper.INSTANCE;

    @Test
    void shouldMapEntityToDto() {
        Course courseEntity = Course.builder().id(1L).name("Math").description("Description").build();

        CourseDto courseDto = MAPPER.mapToDto(courseEntity);

        assertThat(courseDto.getId()).isEqualTo(courseEntity.getId());
        assertThat(courseDto.getName()).isEqualTo(courseEntity.getName());
        assertThat(courseDto.getDescription()).isEqualTo(courseEntity.getDescription());
    }

    @Test
    void shouldMapDtoToEntity() {
        CourseDto courseDto = CourseDto.builder().id(1L).name("Math").description("Description").build();

        Course courseEntity = MAPPER.mapToEntity(courseDto);

        assertThat(courseEntity.getName()).isEqualTo(courseDto.getName());
        assertThat(courseEntity.getId()).isEqualTo(courseDto.getId());
        assertThat(courseEntity.getDescription()).isEqualTo(courseDto.getDescription());
    }

    @Test
    void shouldUpdateEntityWithDto() {
        CourseDto courseDto =  CourseDto.builder().id(1L).name("Biology").description("Description Updated").build();
        Course courseEntity =  Course.builder().id(1L).name("Math").description("Description").build();

        MAPPER.updateCourseFromDto(courseDto, courseEntity);

        assertThat(courseEntity.getName()).isEqualTo(courseDto.getName());
        assertThat(courseEntity.getId()).isEqualTo(courseDto.getId());
        assertThat(courseEntity.getId()).isEqualTo(courseDto.getId());
    }
}
