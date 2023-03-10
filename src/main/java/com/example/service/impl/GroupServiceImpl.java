package com.example.service.impl;

import com.example.dto.GroupDto;
import com.example.entity.Group;
import com.example.exception.ConflictException;
import com.example.exception.RelationRemovalException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapping.GroupMapper;
import com.example.repository.GroupRepository;
import com.example.service.GroupService;
import com.example.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@Validated
public class GroupServiceImpl implements GroupService {
    private static final GroupMapper MAPPER = GroupMapper.INSTANCE;

    @Autowired
    private GroupRepository groupRepo;

    @Transactional(readOnly = true)
    @Override
    public List<GroupDto> findAll() {
        return groupRepo.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupDto> findAllWithLessOrEqualStudents(Integer studentsQuantity) {
        return groupRepo.findAllByStudentsIsLessThanOrEqual(studentsQuantity)
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto create(GroupDto groupDto) {
        log.info("Creating new group: {}", groupDto);
        validateNameIsUnique(groupDto.getName());
        Group savedGroup = groupRepo.save(MAPPER.mapToEntity(groupDto));
        return MAPPER.mapToDto(savedGroup);
    }

    @Override
    public GroupDto update(Long id, GroupDto groupDto) {
        log.info("Updating group: {}", groupDto);
        Group groupEntity = findGroupEntity(id);
        if (!groupEntity.getName().equals(groupDto.getName())) {
            validateNameIsUnique(groupDto.getName());
        }

        MAPPER.updateGroupFromDto(groupDto, groupEntity);
        return MAPPER.mapToDto(groupRepo.save(groupEntity));
    }

    @Transactional(readOnly = true)
    @Override
    public GroupDto findOne(Long groupId) {
        Group groupEntity = findGroupEntity(groupId);
        return MAPPER.mapToDto(groupEntity);
    }

    @Override
    public void delete(Long groupId) {
        log.info("Deleting group with ID: {}", groupId);
        findGroupEntity(groupId);
        validateNoAssignedStudents(groupId);
        groupRepo.deleteById(groupId);
    }

    private void validateNameIsUnique(String name) {
        groupRepo.findByName(name)
                .ifPresent(cp -> {
                    throw new ConflictException(String.format("Group with name '%s' already exists!", name));
                });
    }

    private Group findGroupEntity(Long id) {
        return groupRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Group with id: %s not found!", id))
                );
    }

    private void validateNoAssignedStudents(Long groupId) {
        var group = groupRepo.findById(groupId).get();
        if (!group.getStudents().isEmpty()) {
            throw new RelationRemovalException(
                    String.format("Can't delete group with assigned students! Students: %s", group.getStudents())
            );
        }
    }
}
