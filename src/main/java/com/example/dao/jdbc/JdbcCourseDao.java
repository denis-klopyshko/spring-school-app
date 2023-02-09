package com.example.dao.jdbc;

import com.example.dao.CourseDao;
import com.example.entity.Course;
import com.example.dao.mapper.CourseRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCourseDao implements CourseDao {
    private static final String DELETE_SQL = "DELETE FROM courses WHERE course_id = ?";
    private static final String INSERT_SQL = "INSERT INTO courses (course_name, course_description) VALUES (?, ?);";
    private static final String UPDATE_SQL = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String FIND_ALL_SQL = "SELECT course_id, course_name, course_description FROM courses";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE course_id = ?";
    private static final String FIND_BY_NAME_SQL = FIND_ALL_SQL + " WHERE course_name = ?";
    private static final String FIND_COURSES_BY_STUDENT_ID_SQL = "" +
            "SELECT c.course_id, c.course_name, c.course_description " +
            "FROM courses c " +
            "INNER JOIN students_courses sc ON c.course_id = sc.course_id " +
            "WHERE sc.student_id = ?";
    private static final String COUNT_RECORDS_SQL = "SELECT count(*) FROM courses";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();

    @Autowired
    public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate
                .query(FIND_ALL_SQL, courseRowMapper);
    }

    @Override
    public List<Course> findAllByStudentId(Long studentId) {
        return jdbcTemplate
                .query(FIND_COURSES_BY_STUDENT_ID_SQL, courseRowMapper, studentId);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return jdbcTemplate
                .query(FIND_BY_ID_SQL, courseRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Course> findByName(String name) {
        return jdbcTemplate
                .query(FIND_BY_NAME_SQL, courseRowMapper, name)
                .stream()
                .findFirst();
    }

    @Override
    public Course create(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"course_id"});
                    ps.setString(1, course.getName());
                    ps.setString(2, course.getDescription());
                    return ps;
                }, keyHolder);
        course.setId(keyHolder.getKeyAs(Long.class));
        return course;
    }

    @Override
    public Course update(Course course) {
        jdbcTemplate
                .update(UPDATE_SQL, course.getName(), course.getDescription(), course.getId());
        return course;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public Long count() {
        return jdbcTemplate
                .queryForObject(COUNT_RECORDS_SQL, Long.class);
    }
}
