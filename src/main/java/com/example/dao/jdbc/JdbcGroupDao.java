package com.example.dao.jdbc;

import com.example.dao.GroupDao;
import com.example.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGroupDao implements GroupDao {
    private static final String INSERT_SQL = "INSERT INTO groups (group_name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE_SQL = "DELETE FROM groups WHERE group_id = ?";
    private static final String FIND_ALL_SQL = "SELECT group_id, group_name FROM groups";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE group_id = ?";
    private static final String GET_BY_STUDENTS_COUNT_SQL =
            "SELECT g.group_id, g.group_name " +
                    "FROM groups g " +
                    "LEFT JOIN students s " +
                    "ON s.group_id = g.group_id " +
                    "GROUP BY g.group_id " +
                    "HAVING COUNT(*) <= ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Group> groupRowMapper;

    public JdbcGroupDao(DataSource dataSource, RowMapper<Group> groupRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.groupRowMapper = groupRowMapper;
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate
                .query(FIND_ALL_SQL, groupRowMapper);
    }

    @Override
    public Optional<Group> findById(Long id) {
        return jdbcTemplate
                .query(FIND_BY_ID_SQL, groupRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Group create(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"group_id"});
                    ps.setString(1, group.getName());
                    return ps;
                }, keyHolder);
        group.setId(keyHolder.getKeyAs(Long.class));
        return group;
    }

    @Override
    public Group update(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(UPDATE_SQL, new String[]{"group_id"});
                    ps.setString(1, group.getName());
                    ps.setLong(2, group.getId());
                    return ps;
                }, keyHolder);
        group.setId(keyHolder.getKeyAs(Long.class));
        return group;
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate
                .update(DELETE_SQL, id) == 1;
    }
}
