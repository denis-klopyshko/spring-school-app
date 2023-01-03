package com.example;

import com.example.dao.CourseDao;
import com.example.dao.GroupDao;
import com.example.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SqlGroup({
        @Sql(scripts = "classpath:sql/test-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/clean.sql", executionPhase = AFTER_TEST_METHOD)
})
@ActiveProfiles("test")
@SpringBootTest
abstract class BaseDaoTest {
    @Autowired
    CourseDao courseDao;

    @Autowired
    StudentDao studentDao;

    @Autowired
    GroupDao groupDao;
}
