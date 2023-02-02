package com.example.sheduled;


import com.example.data.TestDataGenerator;
import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.example.service.CourseService;
import com.example.service.GroupService;
import com.example.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@ConditionalOnProperty(name = "spring.quartz.auto-startup")
public class PopulateTestDataOneTimeJob implements Job {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private CourseService courseService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        var jobAlreadyTriggered = jobExecutionContext.getMergedJobDataMap().containsKey("executed");
        if (!jobAlreadyTriggered) {
            try {
                log.info("Starting PopulateTestDataOneTimeJob!");
                List<GroupDto> createdGroups = addGroups();
                List<CourseDto> createdCourses = addCourses();
                addStudents(createdGroups, createdCourses);
                jobExecutionContext.getJobDetail().getJobDataMap().putAsString("executed", true);
                log.info("Successfully executed PopulateTestDataOneTimeJob!");
            } catch (Exception e) {
                log.error("Error executing PopulateTestDataOneTimeJob!", e);
            }
        } else {
            log.info("PopulateTestDataOneTimeJob was already executed!");
        }
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
