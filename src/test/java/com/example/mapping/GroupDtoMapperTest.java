package com.example.mapping;

import com.example.dto.GroupDto;
import com.example.entity.Group;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroupDtoMapperTest {
    private static final GroupMapper MAPPER = GroupMapper.INSTANCE;

    @Test
    void shouldMapEntityToDto() {
        Group groupEntity = Group.builder().id(1L).name("GR-11").build();

        GroupDto groupDto = MAPPER.mapToDto(groupEntity);

        assertThat(groupDto.getId()).isEqualTo(groupEntity.getId());
        assertThat(groupDto.getName()).isEqualTo(groupEntity.getName());
    }

    @Test
    void shouldMapDtoToEntity() {
        GroupDto groupDto = GroupDto.builder().name("GR-11").build();

        Group groupEntity = MAPPER.mapToEntity(groupDto);

        assertThat(groupEntity.getName()).isEqualTo(groupDto.getName());
        assertThat(groupEntity.getId()).isEqualTo(groupDto.getId());
    }

    @Test
    void shouldUpdateEntityWithDto() {
        GroupDto groupDto = GroupDto.builder().id(1L).name("GR-11").build();
        Group groupEntity = Group.builder().id(1L).name("GR-10").build();

        MAPPER.updateGroupFromDto(groupDto, groupEntity);

        assertThat(groupEntity.getName()).isEqualTo(groupDto.getName());
        assertThat(groupEntity.getId()).isEqualTo(groupDto.getId());
    }
}
