package ru.clevertec.taskspring.dao.impl;

import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import ru.clevertec.taskspring.dao.EntityDAO;
import ru.clevertec.taskspring.entity.Tag;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.clevertec.taskspring.dao.Query.*;

@Repository
public class TagDAO implements EntityDAO<Tag> {

    private final JdbcTemplate jdbcTemplate;

    public TagDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL_TAG, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> findById(int id) {
        return jdbcTemplate.query(FIND_BY_ID_TAG, new BeanPropertyRowMapper<>(Tag.class), id).stream().findAny();
    }

    @Override
    public boolean save(Tag tag) {
        return jdbcTemplate.update(SAVE_TAG, tag.getName()) == 1;
    }

    @Override
    public boolean update(Tag tag) {
        return jdbcTemplate.update(UPDATE_TAG, tag.getName(), tag.getId()) == 1;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_TAG, id) == 1;
    }

    public List<Tag> findByName(String name) {
        return jdbcTemplate.query(FIND_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name);
    }

    public List<Tag> findByPartialName(String partialName) {
        Map<String, Object> result = jdbcTemplate.call(
                callFindByPartialName(partialName),
                List.of(new SqlParameter(Types.VARCHAR)));
        return rowMapper(result);
    }

    private CallableStatementCreator callFindByPartialName(String partialName) {
        return connection -> {
            CallableStatement statement = connection.prepareCall(CALL_GET_BY_PARTIAL_NAME);
            statement.setString(1, partialName);
            return statement;
        };
    }

    private List<Tag> rowMapper(Map<String, Object> map) {
        return (List<Tag>) map.values().stream()
                .flatMap(m -> ((List) m).stream())
                .map(o -> Tag.builder()
                        .id(((Map<String, Integer>) o).get(TAG_ID))
                        .name(((Map<String, String>) o).get(TAG_NAME))
                        .build())
                .collect(Collectors.toList());
    }

}