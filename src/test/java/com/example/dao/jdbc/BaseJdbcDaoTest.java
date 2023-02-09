package com.example.dao.jdbc;

import com.example.dao.mapper.CourseRowMapper;
import com.example.dao.mapper.GroupRowMapper;
import com.example.dao.mapper.StudentRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SqlGroup({
        @Sql(scripts = "classpath:sql/test-data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/clean.sql", executionPhase = AFTER_TEST_METHOD)
})
@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        JdbcStudentDao.class, JdbcCourseDao.class, JdbcGroupDao.class,
        StudentRowMapper.class, CourseRowMapper.class, GroupRowMapper.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BaseJdbcDaoTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected JdbcGroupDao groupDao;
    protected JdbcCourseDao courseDao;
    protected JdbcStudentDao studentDao;

    @BeforeEach
    void setUp() {
        groupDao = new JdbcGroupDao(jdbcTemplate);
        courseDao = new JdbcCourseDao(jdbcTemplate);
        var studentRowMapper = new StudentRowMapper(groupDao, courseDao);
        studentDao = new JdbcStudentDao(jdbcTemplate, studentRowMapper);
    }
}
