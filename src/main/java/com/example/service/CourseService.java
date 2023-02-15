package com.example.service;

import com.example.dto.CourseDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface CourseService {
    List<CourseDto> findAll();

    List<CourseDto> findAllByStudentId(@NotNull Long studentId);

    CourseDto create(@Valid @NotNull CourseDto courseDto);

    CourseDto update(@NotNull Long id, @Valid @NotNull CourseDto courseDto);

    CourseDto findOne(@NotNull Long id);

    void delete(@NotNull Long courseId);
}
