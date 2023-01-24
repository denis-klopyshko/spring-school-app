package com.example;

import com.example.data.TestDataGenerator;
import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.example.service.CourseService;
import com.example.service.GroupService;
import com.example.service.StudentService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class SpringSchoolAppApplication implements ApplicationRunner {

    @Autowired
    StudentService studentService;

    @Autowired
    GroupService groupService;

    @Autowired
    CourseService courseService;

    public static void main(String[] args) {
        SpringApplication.run(SpringSchoolAppApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        List<GroupDto> createdGroups = addGroups();
        List<CourseDto> createdCourses = addCourses();
        addStudents(createdGroups, createdCourses);
    }

    private List<GroupDto> addGroups() {
        List<GroupDto> groups = TestDataGenerator.buildGroups();
        List<GroupDto> createdGroups = new ArrayList<>();
        groups.forEach(groupDto -> {
            GroupDto createdGroup = groupService.create(groupDto);
            createdGroups.add(createdGroup);
        });

        return createdGroups;
    }

    private List<CourseDto> addCourses() {
        List<CourseDto> courses = TestDataGenerator.buildCourses();
        List<CourseDto> createdCourses = new ArrayList<>();
        courses.forEach(course -> {
            CourseDto createdCourse = courseService.create(course);
            createdCourses.add(createdCourse);
        });

        return createdCourses;
    }

    private void addStudents(List<GroupDto> groups, List<CourseDto> courses) {
        List<StudentDto> students = TestDataGenerator.buildStudents();

        for (int i = 0; i < students.size(); i++) {
            StudentDto currentStudent = students.get(i);
            if (i % 9 != 0) {
                currentStudent.setGroup(groups.get(RandomUtils.nextInt(1, 9)));
            }

            Set<CourseDto> studentCourses = pickRandomCourses(courses, RandomUtils.nextInt(1, 4));
            currentStudent.setCourses(new ArrayList<>(studentCourses));
            studentService.create(currentStudent);
        }
    }

    private Set<CourseDto> pickRandomCourses(List<CourseDto> courses, int numberOfElements) {
        Set<CourseDto> result = new HashSet<>();
        for (int i = 0; i < numberOfElements; i++) {
            result.add(courses.get(RandomUtils.nextInt(0, courses.size())));
        }
        return result;
    }
}
