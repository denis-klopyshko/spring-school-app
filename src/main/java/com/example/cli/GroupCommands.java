package com.example.cli;

import com.example.dto.GroupDto;
import com.example.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.*;

import java.util.LinkedHashMap;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GroupCommands {
    private final GroupService groupService;
    private final ConsoleWriter consoleWriter;

    @ShellMethod(value = "Find all Groups with less or equal students.", key = "find-groups")
    public Table findAllGroupsWithLessOrEqualStudents(Integer studentsQuantity) {
        List<GroupDto> groups = groupService.findAllWithLessOrEqualStudents(studentsQuantity);
        consoleWriter.printInfo(String.format("Found %s Groups with less than %s or equal Students", groups.size(), studentsQuantity));
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Group Name");

        TableModel tableModel = new BeanListTableModel<>(groups, headers);
        TableBuilder table = new TableBuilder(tableModel);
        table.addFullBorder(BorderStyle.fancy_light);
        table.on(CellMatchers.table()).addAligner(SimpleHorizontalAligner.left);
        table.on(CellMatchers.column(0)).addSizer(new AbsoluteWidthSizeConstraints(5));
        table.on(CellMatchers.column(1)).addSizer(new AbsoluteWidthSizeConstraints(15));

        return table.build();
    }
}
