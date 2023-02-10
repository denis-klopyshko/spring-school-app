package com.example.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @Builder.Default
    @ToString.Exclude
    private List<Student> students = new ArrayList<>();

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Course(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Course ofId(Long id) {
        return Course.builder().id(id).build();
    }
}

