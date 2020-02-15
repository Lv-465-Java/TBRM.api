package com.softserve.rms.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GroupMemberRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long userId, Long groupId) {
        jdbcTemplate.update(
                "INSERT INTO groups_members (user_id, group_id) VALUES (?, ?)",
                userId, groupId
        );
    }


    public void delete(Long userId, Long groupId) {
        jdbcTemplate.update(
                "DELETE FROM groups_members WHERE user_id = ? AND group_id = ?",
                userId, groupId
        );
    }

    public void deleteGroup(String groupName) {
        jdbcTemplate.update(
          "DELETE FROM groups WHERE name = ? RETURNING *",
                groupName
        );
    }
}
