package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootApplication
@EnableScheduling
public class SpringSchoolAppApplication implements ApplicationRunner {

//    @Autowired
//    JpaStudentDao jpaStudentDao;
//
//    @Autowired
//    JpaGroupDao jpaGroupDao;

    public static void main(String[] args) {
        SpringApplication.run(SpringSchoolAppApplication.class, args);
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        Student student = jpaStudentDao.findById(1L).get();
//        System.out.println(student.getCourses());
//
//
//        List<Group> groups = jpaGroupDao.findAllWithLessOrEqualStudents(1L);
//        System.out.println("GROUPS" + groups);
    }
}
