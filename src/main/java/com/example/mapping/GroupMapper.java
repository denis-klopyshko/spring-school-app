package com.example.mapping;

import com.example.dto.GroupDto;
import com.example.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDto mapToDto(Group group);

    Group mapToEntity(GroupDto groupDto);

    void updateGroupFromDto(GroupDto groupDto, @MappingTarget Group group);
}
