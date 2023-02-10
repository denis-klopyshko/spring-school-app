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


    public static void main(String[] args) {
        SpringApplication.run(SpringSchoolAppApplication.class, args);
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
