package com.example;

import com.example.dto.StudentDto;
import com.example.service.CourseService;
import com.example.service.GroupService;
import com.example.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class SpringSchoolAppApplication implements ApplicationRunner {

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @Autowired
    GroupService groupService;


    public static void main(String[] args) {
        SpringApplication.run(SpringSchoolAppApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        studentService.delete(209L);
    }
}
