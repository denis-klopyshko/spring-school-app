package com.example.service;

import com.example.dto.GroupDto;
import com.example.entity.Group;
import com.example.entity.Student;
import com.example.exception.ConflictException;
import com.example.exception.RelationRemovalException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.GroupRepository;
import com.example.service.impl.GroupServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {StudentServiceImpl.class, GroupServiceImpl.class})
class GroupServiceImplTest {
    @MockBean
    private GroupRepository groupRepo;

    @Autowired
    private GroupServiceImpl groupService;

    @Test
    void shouldCreateNewGroup() {
        Group group = getGroupEntity();

        when(groupRepo.findByName(anyString())).thenReturn(Optional.empty());
        when(groupRepo.save(any(Group.class))).thenReturn(group);

        GroupDto groupDto = groupService.create(GroupDto.builder().name("GR-10").build());
        assertThat(groupDto.getName()).isEqualTo("GR-10");
        assertThat(groupDto.getId()).isEqualTo(1L);

        verify(groupRepo).save(any(Group.class));
    }

    @Test
    void shouldNotCreateGroupAlreadyExists() {
        Group group = getGroupEntity();

        when(groupRepo.findByName(anyString())).thenReturn(Optional.of(group));

        assertThatThrownBy(() -> groupService.create(GroupDto.builder().name("GR-10").build()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Group with name '%s' already exists!", group.getName());
    }

    @Test
    void shouldNotUpdateGroupNotFound() {
        when(groupRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.update(1L, GroupDto.builder().id(1L).name("GR-10").build()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Group with id: 1 not found!");
    }

    @Test
    void shouldUpdateGroupName() {
        Group groupBeforeUpdate = getGroupEntity();
        Group groupAfterUpdate = getGroupEntity();
        groupAfterUpdate.setName("GR-11");

        when(groupRepo.findById(groupBeforeUpdate.getId())).thenReturn(Optional.of(groupBeforeUpdate));
        when(groupRepo.save(any(Group.class))).thenReturn(groupAfterUpdate);

        GroupDto groupDto = groupService.update(1L, GroupDto.builder().id(1L).name("GR-11").build());
        assertThat(groupDto.getName()).isEqualTo("GR-11");
    }

    @Test
    void shouldFailToDelete_groupNotFound() {
        when(groupRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Group with id: 1 not found!");
    }

    @Test
    void shouldFailToDelete_assignedStudents() {
        Group group = getGroupEntity();
        when(groupRepo.findById(1L)).thenReturn(Optional.of(group));
        when(group.getStudents()).thenReturn(List.of(new Student()));

        assertThatThrownBy(() -> groupService.delete(1L))
                .isInstanceOf(RelationRemovalException.class)
                .hasMessageContaining("Can't delete group with assigned students! Students:");
    }

    @Test
    void shouldDeleteGroup() {
        Group group = getGroupEntity();
        when(groupRepo.findById(1L)).thenReturn(Optional.of(group));
        when(group.getStudents()).thenReturn(emptyList());

        groupService.delete(1L);

        verify(groupRepo).deleteById(1L);
    }

    private Group getGroupEntity() {
        return Group.builder().id(1L).name("GR-10").build();
    }
}
