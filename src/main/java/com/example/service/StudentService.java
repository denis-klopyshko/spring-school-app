package com.example.service;

import com.example.dto.StudentDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface StudentService {
    List<StudentDto> findAll();

    List<StudentDto> findAllByGroupId(Long groupId);

    StudentDto create(@Valid @NotNull StudentDto studentDto);

    StudentDto update(@NotNull Long id, @Valid @NotNull StudentDto studentDto);

    StudentDto findOne(@NotNull Long id);

    void delete(@NotNull Long courseId);
}
