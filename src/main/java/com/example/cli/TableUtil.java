package com.example.cli;

import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import lombok.experimental.UtilityClass;
import org.springframework.shell.table.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class TableUtil {
    @SuppressWarnings("unchecked")
    public static Table buildStudentsTable(List<StudentDto> studentsList) {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("firstName", "First Name");
        headers.put("lastName", "Last Name");
        headers.put("group", "Group");
        headers.put("courses", "Courses");

        TableModel tableModel = new BeanListTableModel<>(studentsList, headers);
        TableBuilder table = new TableBuilder(tableModel);
        table.addInnerBorder(BorderStyle.fancy_light);
        table.addHeaderBorder(BorderStyle.fancy_double);
        table.on(CellMatchers.ofType(List.class))
                .addFormatter(value -> new String[]{
                        ((List<CourseDto>) value)
                                .stream()
                                .map(CourseDto::getName)
                                .collect(Collectors.joining(", "))
                })
                .addSizer(new AbsoluteWidthSizeConstraints(50));

        table.on(CellMatchers.ofType(GroupDto.class)).addFormatter(value -> new String[]{
                Optional.ofNullable(value)
                        .map(GroupDto.class::cast)
                        .map(groupDto -> String.format("ID: %s,Name: %s", groupDto.getId(), groupDto.getName()))
                        .orElse("null")
        }).addSizer(new AbsoluteWidthSizeConstraints(20));

        return table.build();
    }
}
