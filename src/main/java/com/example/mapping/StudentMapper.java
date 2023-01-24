package com.example.mapping;

import com.example.dto.StudentDto;
import com.example.entity.Student;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CourseMapper.class, GroupMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDto mapToDto(Student student);

    Student mapToEntity(StudentDto studentDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Student mapBaseAttributes(StudentDto studentDto);


    @Mapping(target = "courses", ignore = true)
    void updateStudentFromDto(StudentDto studentDto, @MappingTarget Student student);
}
