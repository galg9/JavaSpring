package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.entity.Locker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LockerRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class LockerRowMapper implements RowMapper<Locker> {
        @Override
        public Locker mapRow(ResultSet rs, int rowNum) throws SQLException {
            Locker locker = new Locker();
            locker.setId(rs.getObject("l_id", UUID.class));
            locker.setNumber(rs.getInt("l_number"));
            locker.setCreatedDatetime(rs.getObject("l_created_datetime", LocalDateTime.class));
            locker.setUpdatedDatetime(rs.getObject("l_updated_datetime", LocalDateTime.class));
            return locker;
        }
    }

    private static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            UUID clientId = rs.getObject("id", UUID.class);
            if (clientId == null) {
                return null;
            }
            client.setId(clientId);
            client.setSurname(rs.getString("surname"));
            client.setName(rs.getString("name"));
            client.setPatronymic(rs.getString("patronymic"));
            client.setBirthday(rs.getObject("birthday", LocalDate.class));
            client.setPhone(rs.getString("phone"));
            client.setEmail(rs.getString("email"));
            client.setIsActive(rs.getBoolean("is_active"));
            client.setCreatedDatetime(rs.getObject("created_datetime", LocalDateTime.class));
            client.setUpdatedDatetime(rs.getObject("updated_datetime", LocalDateTime.class));
            return client;
        }
    }

    private static class LockerWithClientRowMapper implements RowMapper<Locker> {
        @Override
        public Locker mapRow(ResultSet rs, int rowNum) throws SQLException {
            Locker locker = new LockerRowMapper().mapRow(rs, rowNum);
            Client client = new ClientRowMapper().mapRow(rs, rowNum);

            if (client != null && client.getId() != null) {
                locker.setClient(client);
            }
            return locker;
        }
    }

    public Locker findById(UUID id) {
        log.debug("Поиск шкафчика по id: {}", id);
        String sql = """
                SELECT c.*,
                    l.id as l_id,
                    l.number as l_number,
                    l.created_datetime as l_created_datetime,
                    l.updated_datetime as l_updated_datetime
                FROM lockers l LEFT JOIN clients c ON l.client_id = c.id WHERE l.id = ?
                """;
        List<Locker> lockers = jdbcTemplate.query(sql, new LockerWithClientRowMapper(), id);
        return lockers.isEmpty() ? null : lockers.get(0);
    }

    public List<Locker> findAllWithClientInfo() {
        log.debug("Получение списка всех шкафчиков с информацией о клиентах");
        String sql = """
                SELECT c.*,
                    l.id as l_id,
                    l.number as l_number,
                    l.created_datetime as l_created_datetime,
                    l.updated_datetime as l_updated_datetime
                FROM lockers l LEFT JOIN clients c ON l.client_id = c.id ORDER BY l.number
                """;
        return jdbcTemplate.query(sql, new LockerWithClientRowMapper());
    }

    public void update(Locker locker) {
        log.debug("Обновление шкафчика с id: {}", locker.getId());
        String sql = "UPDATE lockers SET client_id = ?, updated_datetime = ? WHERE id = ?";
        UUID clientId = Optional.ofNullable(locker.getClient())
                .map(Client::getId)
                .orElse(null);

        jdbcTemplate.update(sql, clientId, LocalDateTime.now(), locker.getId());
    }
}