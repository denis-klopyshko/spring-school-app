package com.example.dao.mapper;

import com.example.dao.jdbc.JdbcCourseDao;
import com.example.dao.jdbc.JdbcGroupDao;
import com.example.entity.Course;
import com.example.entity.Group;
import com.example.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentRowMapper implements RowMapper<Student> {
    private final JdbcGroupDao groupDao;
    private final JdbcCourseDao courseDao;

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        Long id = rs.getLong("student_id");
        Group group = groupDao.findById(rs.getLong("group_id")).orElse(null);
        List<Course> courseList = courseDao.findAllByStudentId(id);

        student.setId(id);
        student.setGroup(group);
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setCourses(courseList);

        return student;
    }
}
