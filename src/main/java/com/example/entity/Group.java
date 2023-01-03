package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private Long id;
    private String name;

    public Group(String name) {
        this.name = name;
    }

    public static Group ofId(Long id) {
        return Group.builder().id(id).build();
    }
}

