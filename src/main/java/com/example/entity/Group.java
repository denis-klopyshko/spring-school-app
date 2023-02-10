package com.example.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    @Builder.Default
    @ToString.Exclude
    List<Student> students = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public Group(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public static Group ofId(Long id) {
        return Group.builder().id(id).build();
    }
}

