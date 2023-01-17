package com.example.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;
    private Group group;
    private String firstName;
    private String lastName;

    @Builder.Default
    private List<Course> courses = new ArrayList<>();

    public Student(Long id, Group group, String firstName, String lastName) {
        this.id = id;
        this.group = group;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(Group group, String firstName, String lastName) {
        this(null, group, firstName, lastName);
    }

    public Student(String firstName, String lastName) {
        this(null, firstName, lastName);
    }
}
