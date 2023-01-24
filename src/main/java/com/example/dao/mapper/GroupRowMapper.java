package com.example.dao.mapper;

import com.example.entity.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();

        group.setId(rs.getLong("group_id"));
        group.setName(rs.getString("group_name"));

        return group;
    }
}
