package com.example.cli;

import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.example.service.GroupService;
import com.example.service.StudentService;
import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.Table;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ShellComponent
public class StudentCommands {
    @Autowired
    @Lazy
    private LineReader lineReader;

    @Autowired
    private ConsoleWriter consoleWriter;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @ShellMethod(value = "Find Student by id.", key = "find-student")
    public void findStudentById(Long id) throws IOException {
        lineReader.getHistory().purge();
        StudentDto studentDto = studentService.findOne(id);
        Table studentsTable = TableUtil.buildStudentsTable(List.of(studentDto));
        consoleWriter.print(studentsTable.render(80));
    }

    @ShellMethod(value = "Find all students by group id.", key = "find-students")
    public void findStudentsByGroupId(@ShellOption(value = {"--groupId"}) Long groupId) {
        List<StudentDto> students = studentService.findAllByGroupId(groupId);
        consoleWriter.printInfo("Found Students: " + students.size());

        Table studentsTable = TableUtil.buildStudentsTable(students);
        consoleWriter.print(studentsTable.render(80));
    }

    @ShellMethod(value = "Find all students assigned on Course.", key = "find-students")
    public void findStudentsByCourseName(@ShellOption(value = {"--course"}) String name) {
        List<StudentDto> students = studentService.findAllByCourseName(name);
        consoleWriter.printInfo("Found Students: " + students.size());

        Table studentsTable = TableUtil.buildStudentsTable(students);
        consoleWriter.print(studentsTable.render(80));
    }

    @ShellMethod(value = "Delete Student by id.", key = "delete-student")
    public void deleteStudent(@ShellOption(value = {"--id"}) Long id) {
        studentService.delete(id);
        consoleWriter.printSuccess(String.format("Student with ID: [%s] successfully deleted!", id));
    }

    @ShellMethod(value = "Add new Student", key = "add-student")
    public void addStudent() {
        StudentDto student = new StudentDto();
        do {
            String firstName = lineReader.readLine("Enter First Name: ");
            if (StringUtils.hasText(firstName)) {
                student.setFirstName(firstName);
            }

        } while (student.getFirstName() == null);

        do {
            String lastName = lineReader.readLine("Enter Last Name: ");
            if (StringUtils.hasText(lastName)) {
                student.setLastName(lastName);
            }

        } while (student.getLastName() == null);

        List<GroupDto> availableGroups = groupService.findAll();
        for (int i = 0; i < availableGroups.size(); i++) {
            consoleWriter.print(String.format("%d. %s", i + 1, availableGroups.get(i).getName()));
        }

        String studentGroup = lineReader.readLine("Type Group number to add student to group: ");
        Optional.ofNullable(studentGroup)
                .ifPresentOrElse(
                        number -> student.setGroup(availableGroups.get(Integer.parseInt(number) - 1)),
                        () -> student.setGroup(null)
                );

        StudentDto createdStudent = studentService.create(student);
        Table studentTable = TableUtil.buildStudentsTable(List.of(createdStudent));
        consoleWriter.print(studentTable.render(80));
    }

    @ShellMethod(value = "Assign or Remove Student Course.", key = "update-student")
    public void updateStudentOnCourse(
            Long studentId,
            @ShellOption(value = {"--remove, -r"}, defaultValue = "false") boolean remove,
            Long courseId
    ) {
        if (remove) {
            studentService.removeStudentFromCourse(studentId, courseId);
            consoleWriter.printSuccess(
                    String.format("Student with ID: [%s] successfully removed from Course with ID: [%s]", studentId, courseId)
            );
        } else {
            studentService.assignStudentOnCourse(studentId, courseId);
            consoleWriter.printSuccess(
                    String.format("Student with ID: [%s] successfully assigned on Course with ID: [%s]", studentId, courseId)
            );
        }

        StudentDto createdStudent = studentService.findOne(studentId);
        Table studentTable = TableUtil.buildStudentsTable(List.of(createdStudent));
        consoleWriter.print(studentTable.render(80));
    }
}
