package ru.clevertec.taskspring.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;
import ru.clevertec.taskspring.dao.EntityDAO;
import ru.clevertec.taskspring.entity.GiftCertificate;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.clevertec.taskspring.dao.Query.*;

@Repository
public class GiftCertificateDAO implements EntityDAO<GiftCertificate> {

    private final JdbcTemplate jdbcTemplate;

    public GiftCertificateDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(FIND_ALL_GC, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public Optional<GiftCertificate> findById(int id) {
        return jdbcTemplate.query(FIND_BY_ID_GC, new BeanPropertyRowMapper<>(GiftCertificate.class), id)
                .stream().findAny();
    }

    @Override
    public boolean save(GiftCertificate cert) {
        return jdbcTemplate.update(SAVE_GC, cert.getName(), cert.getDescription(), cert.getPrice(),
                cert.getDuration(), cert.getCreateDate(), cert.getLastUpdateDate()) == 1;
    }

    @Override
    public boolean update(GiftCertificate cert) {
        return jdbcTemplate.update(UPDATE_GC, cert.getName(), cert.getDescription(), cert.getPrice(),
                cert.getDuration(), cert.getCreateDate(), cert.getLastUpdateDate(), cert.getId()) == 1;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_GC, id) == 1;
    }

    public List<GiftCertificate> findByPartialDescription(String partialDescription) {
        Map<String, Object> result = jdbcTemplate.call(
                callFindByPartialDescription(partialDescription),
                List.of(new SqlParameter(Types.VARCHAR)));
        return rowMapper(result);
    }

    private CallableStatementCreator callFindByPartialDescription(String partialDescription) {
        return connection -> {
            CallableStatement statement = connection.prepareCall(CALL_GET_BY_PARTIAL_DESCRIPTION);
            statement.setString(1, partialDescription);
            return statement;
        };
    }

    private List<GiftCertificate> rowMapper(Map<String, Object> map) {
        return (List<GiftCertificate>) map.values().stream()
                .flatMap(m -> ((List) m).stream())
                .map(o -> GiftCertificate.builder()
                        .id(((Map<String, Integer>) o).get(GC_ID))
                        .name(((Map<String, String>) o).get(GC_NAME))
                        .description(((Map<String, String>) o).get(GC_DESCRIPTION))
                        .price(((Map<String, Double>) o).get(GC_PRICE))
                        .duration(((Map<String, Integer>) o).get(GC_DURATION))
                        .createDate(((Map<String, Date>) o).get(GC_CREATE_DATE))
                        .lastUpdateDate(((Map<String, Date>) o).get(GC_LAST_UPDATE_DATE))
                        .build())
                .collect(Collectors.toList());
    }

}