package com.example.dao.jdbc;

import com.example.dao.StudentDao;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.exception.DaoOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
public class JdbcStudentDao implements StudentDao {
    @Value("${database.insert.batch-size:30}")
    private final int BATCH_SIZE = 30;
    private static final String INSERT_SQL = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
    private static final String UPDATE_SQL = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE_STUDENT_SQL = "DELETE FROM students WHERE student_id = ?";
    private static final String FIND_ALL_SQL = "" +
            "SELECT s.student_id, s.group_id, s.first_name, s.last_name, g.group_id, g.group_name " +
            "FROM students s " +
            "LEFT JOIN groups g on s.group_id = g.group_id";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE s.student_id = ?";
    private static final String INSERT_STUDENT_COURSE_SQL = "INSERT INTO students_courses (student_id, course_id) VALUES ( ?, ? )";
    private static final String DELETE_ALL_STUDENT_COURSES_SQL = "DELETE FROM students_courses WHERE student_id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> studentRowMapper;

    @Autowired
    public JdbcStudentDao(DataSource dataSource, RowMapper<Student> studentRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentRowMapper = studentRowMapper;
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate
                .query(FIND_ALL_SQL, studentRowMapper);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return jdbcTemplate
                .query(FIND_BY_ID_SQL, studentRowMapper, id)
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
        Long id = keyHolder.getKeyAs(Long.class);

        jdbcTemplate.batchUpdate(INSERT_STUDENT_COURSE_SQL, student.getCourses(), BATCH_SIZE,
                (PreparedStatement ps, Course course) -> {
                    ps.setLong(1, id);
                    ps.setLong(2, course.getId());
                });

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

        if (!student.getCourses().isEmpty()) {
            updateStudentCourses(student);
        }

        student.setId(keyHolder.getKeyAs(Long.class));
        return student;
    }

    private void updateStudentCourses(Student student) {
        jdbcTemplate.update(DELETE_ALL_STUDENT_COURSES_SQL, student.getId());
        jdbcTemplate.batchUpdate(INSERT_STUDENT_COURSE_SQL, student.getCourses(), BATCH_SIZE,
                (PreparedStatement ps, Course course) -> {
                    ps.setLong(1, student.getId());
                    ps.setLong(1, course.getId());
                });
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
    public boolean deleteById(Long id) {
        return jdbcTemplate
                .update(DELETE_STUDENT_SQL, id) == 1;
    }

    private PreparedStatement fillStatementWithAccountData(PreparedStatement insertStatement, Student student)
            throws SQLException {
        if (isNull(student.getGroup())) {
            insertStatement.setNull(1, java.sql.Types.NULL);
        } else {
            insertStatement.setLong(1, student.getGroup().getId());
        }

        insertStatement.setString(2, student.getFirstName());
        insertStatement.setString(3, student.getLastName());
        return insertStatement;
    }
}
