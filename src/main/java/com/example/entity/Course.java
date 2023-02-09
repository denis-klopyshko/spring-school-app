package com.example.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
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

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Course ofId(Long id) {
        return Course.builder().id(id).build();
    }
}

