package com.example.mapper;

import com.example.entity.Course;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseRowMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();

        course.setId(rs.getLong("course_id"));
        course.setName(rs.getString("course_name"));
        course.setDescription(rs.getString("course_description"));

        return course;
    }
}
