package com.example.service.impl;

import com.example.dao.CourseDao;
import com.example.dao.jdbc.JdbcCourseDao;
import com.example.dto.CourseDto;
import com.example.entity.Course;
import com.example.exception.ConflictException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapping.CourseMapper;
import com.example.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final CourseMapper MAPPER = CourseMapper.INSTANCE;
    private final JdbcCourseDao courseDao;

    @Override
    public List<CourseDto> findAll() {
        return courseDao.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        log.info("Creating new course: {}", courseDto);
        validateNameIsUnique(courseDto.getName());
        var course = MAPPER.mapToEntity(courseDto);

        return MAPPER.mapToDto(courseDao.create(course));
    }

    @Override
    public CourseDto update(Long id, CourseDto courseDto) {
        log.info("Updating course: {}", courseDto);
        var courseEntity = findCourseEntity(id);
        if (!courseEntity.getName().equals(courseDto.getName())) {
            validateNameIsUnique(courseDto.getName());
        }

        MAPPER.updateCourseFromDto(courseDto, courseEntity);
        return MAPPER.mapToDto(courseDao.update(courseEntity));
    }

    @Override
    public CourseDto findOne(Long courseId) {
        var courseEntity = findCourseEntity(courseId);
        return MAPPER.mapToDto(courseEntity);
    }

    @Override
    public void delete(Long courseId) {
        log.info("Deleting course with ID [{}]", courseId);
        findCourseEntity(courseId);
        courseDao.deleteById(courseId);
    }

    private void validateNameIsUnique(String name) {
        courseDao.findByName(name)
                .ifPresent(cp -> {
                    throw new ConflictException(String.format("Course with name '%s' already exists!", name));
                });
    }

    private Course findCourseEntity(Long id) {
        return courseDao.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Course with id: %s not found!", id))
                );
    }
}
