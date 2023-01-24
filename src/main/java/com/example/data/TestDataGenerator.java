package com.example.data;

import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class TestDataGenerator {
    private static final Faker FAKER = new Faker();
    private static final int STUDENTS_QUANTITY = 200;
    private static final int GROUPS_QUANTITY = 10;
    private static final int COURSES_QUANTITY = 10;

    public static List<GroupDto> buildGroups() {
        return IntStream.rangeClosed(1, GROUPS_QUANTITY)
                .mapToObj(i -> GroupDto.builder().name("GR-" + (i + 10)).build())
                .collect(Collectors.toList());
    }

    public static List<CourseDto> buildCourses() {
        return IntStream.rangeClosed(1, COURSES_QUANTITY)
                .mapToObj(i -> CourseDto.builder()
                        .name("Course" + i)
                        .description("Description" + i)
                        .build()
                )
                .collect(Collectors.toList());
    }

    public static List<StudentDto> buildStudents() {
        return IntStream.rangeClosed(1, STUDENTS_QUANTITY)
                .mapToObj(i -> StudentDto
                        .builder()
                        .firstName(FAKER.name().firstName())
                        .lastName(FAKER.name().lastName())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
