package com.example.dto;

import com.example.entity.Student;
import lombok.*;
import net.bytebuddy.matcher.FilterableList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CourseDto {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String description;

    @ToString.Exclude
    @Builder.Default
    private List<@NotNull Student> students = new ArrayList<>();
    public static CourseDto ofId(Long courseId) {
        return CourseDto.builder().id(courseId).build();
    }

    public CourseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
