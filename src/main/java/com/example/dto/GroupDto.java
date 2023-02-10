package com.example.dto;

import com.example.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private static final String GROUP_NAME_PATTERN = "^[aA-zZ]{2}-\\d{2}";

    private Long id;

    @NotBlank
    @Pattern(regexp = GROUP_NAME_PATTERN)
    private String name;

    @Builder.Default
    private List<@NotNull Student> students = new ArrayList<>();

    public static GroupDto ofId(Long id) {
        return GroupDto.builder().id(id).build();
    }
}
