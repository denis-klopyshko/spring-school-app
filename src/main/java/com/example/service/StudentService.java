package com.example.service;

import com.example.dto.StudentDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface StudentService {
    List<StudentDto> findAll();

    List<StudentDto> findAllByGroupId(@NotNull Long groupId);

    List<StudentDto> findAllByCourseName(@NotNull String courseName);

    StudentDto create(@Valid @NotNull StudentDto studentDto);

    StudentDto update(@NotNull Long id, @Valid @NotNull StudentDto studentDto);

    StudentDto findOne(@NotNull Long id);

    void delete(@NotNull Long courseId);

    void assignStudentOnCourse(@NotNull Long studentId, @NotNull Long courseId);

    void removeStudentFromCourse(@NotNull Long studentId, @NotNull Long courseId);
}
