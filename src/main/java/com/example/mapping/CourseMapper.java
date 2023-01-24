package com.example.mapping;

import com.example.dto.CourseDto;
import com.example.entity.Course;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDto mapToDto(Course course);

    Course mapToEntity(CourseDto courseDto);

    @IterableMapping(elementTargetType = Course.class)
    ArrayList<Course> mapAsList(List<CourseDto> courseDtoList);

    void updateCourseFromDto(CourseDto courseDto, @MappingTarget Course course);
}
