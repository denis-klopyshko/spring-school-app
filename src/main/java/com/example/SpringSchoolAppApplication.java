package com.example;

import com.example.dao.CourseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSchoolAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringSchoolAppApplication.class, args);
    }

    @Autowired
    CourseDao courseDao;

    @Override
    public void run(String... args) {
    }
}
