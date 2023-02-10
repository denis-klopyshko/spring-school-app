package com.example.service;

import com.example.dto.GroupDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();

    List<GroupDto> findAllWithLessOrEqualStudents(@NotNull Integer studentsQuantity);

    GroupDto create(@Valid @NotNull GroupDto groupDto);

    GroupDto update(@NotNull Long id, @Valid @NotNull GroupDto groupDto);

    GroupDto findOne(@NotNull Long groupId);

    void delete(@NotNull Long groupId);
}
