package com.example.service.impl;

import com.example.dto.CourseDto;
import com.example.entity.Course;
import com.example.exception.ConflictException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapping.CourseMapper;
import com.example.repository.CourseRepository;
import com.example.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final CourseMapper MAPPER = CourseMapper.INSTANCE;
    private final CourseRepository courseRepo;

    @Override
    public List<CourseDto> findAll() {
        return courseRepo.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        log.info("Creating new course: {}", courseDto);
        validateNameIsUnique(courseDto.getName());
        var course = MAPPER.mapToEntity(courseDto);

        return MAPPER.mapToDto(courseRepo.save(course));
    }

    @Override
    public CourseDto update(Long id, CourseDto courseDto) {
        log.info("Updating course: {}", courseDto);
        var courseEntity = findCourseEntity(id);
        if (!courseEntity.getName().equals(courseDto.getName())) {
            validateNameIsUnique(courseDto.getName());
        }

        MAPPER.updateCourseFromDto(courseDto, courseEntity);
        return MAPPER.mapToDto(courseRepo.save(courseEntity));
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
        courseRepo.deleteById(courseId);
    }

    private void validateNameIsUnique(String name) {
        courseRepo.findByName(name)
                .ifPresent(cp -> {
                    throw new ConflictException(String.format("Course with name '%s' already exists!", name));
                });
    }

    private Course findCourseEntity(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Course with id: %s not found!", id))
                );
    }
}
