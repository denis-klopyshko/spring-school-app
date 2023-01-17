package com.example.dao.jdbc;

import com.example.dao.StudentDao;
import com.example.entity.Group;
import com.example.entity.Student;
import com.example.exception.DaoOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
public class JdbcStudentDao implements StudentDao {
    private static final String FIND_ALL_SQL = "" +
            "SELECT s.student_id, s.group_id, s.first_name, s.last_name, g.group_id, g.group_name " +
            "FROM students s " +
            "LEFT JOIN groups g on s.group_id = g.group_id";
    private static final String INSERT_SQL = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
    private static final String UPDATE_SQL = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE_STUDENT_SQL = "DELETE FROM students WHERE student_id = ?";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE s.student_id = ?";
    private static final String FIND_ALL_BY_GROUP_ID_SQL = FIND_ALL_SQL + " WHERE g.group_id = ?";
    private static final String FIND_BY_FIRSTNAME_AND_LASTNAME_SQL = FIND_ALL_SQL + " WHERE s.first_name = ? AND s.last_name = ?";
    private static final String INSERT_STUDENT_COURSE_SQL = "INSERT INTO students_courses (student_id, course_id) VALUES ( ?, ? )";
    private static final String DELETE_STUDENT_COURSE_SQL = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";
    private static final String DELETE_STUDENT_COURSES_SQL = "DELETE FROM students_courses WHERE student_id = ?";
    private static final String COUNT_RECORDS_SQL = "SELECT count(*) FROM students";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> studentRowMapper;

    @Autowired
    public JdbcStudentDao(JdbcTemplate jdbcTemplate, RowMapper<Student> studentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentRowMapper = studentRowMapper;
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate
                .query(FIND_ALL_SQL, studentRowMapper);
    }

    @Override
    public List<Student> findAllByGroupId(Long groupId) {
        return jdbcTemplate
                .query(FIND_ALL_BY_GROUP_ID_SQL, studentRowMapper);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return jdbcTemplate
                .query(FIND_BY_ID_SQL, studentRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Student> findByFirstNameAndLastName(String firstName, String lastName) {
        return jdbcTemplate
                .query(FIND_BY_FIRSTNAME_AND_LASTNAME_SQL, studentRowMapper, firstName, lastName)
                .stream()
                .findFirst();
    }

    @Override
    @Transactional
    public Student create(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> prepareInsertStatement(connection, student),
                keyHolder);
        student.setId(keyHolder.getKeyAs(Long.class));
        return student;
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Student student) {
        try {
            var insertStatement = connection.prepareStatement(INSERT_SQL, new String[]{"student_id"});
            return fillStatementWithAccountData(insertStatement, student);
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to insert student", e);
        }
    }

    @Override
    @Transactional
    public Student update(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> prepareUpdateStatement(connection, student), keyHolder);
        student.setId(keyHolder.getKeyAs(Long.class));
        return student;
    }

    private PreparedStatement prepareUpdateStatement(Connection connection, Student student) {
        try {
            var updateStatement = connection.prepareStatement(UPDATE_SQL, new String[]{"student_id"});
            fillStatementWithAccountData(updateStatement, student);
            updateStatement.setLong(4, student.getId());
            return updateStatement;
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to update student", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_STUDENT_SQL, id);
    }

    @Override
    public boolean assignStudentOnCourse(Long studentId, Long courseId) {
        return jdbcTemplate
                .update(INSERT_STUDENT_COURSE_SQL, studentId, courseId) == 1;
    }

    @Override
    public boolean removeStudentFromCourse(Long studentId, Long courseId) {
        return jdbcTemplate
                .update(DELETE_STUDENT_COURSE_SQL, studentId, courseId) == 1;
    }

    @Override
    public void removeStudentCourses(Long studentId) {
        jdbcTemplate
                .update(DELETE_STUDENT_COURSES_SQL, studentId);
    }

    @Override
    public Long count() {
        return jdbcTemplate
                .queryForObject(COUNT_RECORDS_SQL, Long.class);
    }

    private PreparedStatement fillStatementWithAccountData(PreparedStatement insertStatement, Student student)
            throws SQLException {
        Long groupId = Optional.ofNullable(student.getGroup()).map(Group::getId).orElse(null);
        insertStatement.setObject(1, groupId);
        insertStatement.setString(2, student.getFirstName());
        insertStatement.setString(3, student.getLastName());
        return insertStatement;
    }
}
