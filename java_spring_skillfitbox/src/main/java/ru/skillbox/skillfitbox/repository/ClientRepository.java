package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.entity.Locker;
import ru.skillbox.skillfitbox.entity.Trainer;
import ru.skillbox.skillfitbox.entity.TrainerStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(UUID.fromString(rs.getString("id")));
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

    public Client save(Client client) {
        log.debug("Сохранение клиента: {} {}", client.getSurname(), client.getName());
        String sql = "INSERT INTO clients (id, surname, name, patronymic, birthday, phone, email, " +
                "is_active, locker_id, created_datetime, updated_datetime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        UUID lockerId = Optional.ofNullable(client.getLocker())
                .map(Locker::getId)
                .orElse(null);

        jdbcTemplate.update(sql, id, client.getSurname(), client.getName(), client.getPatronymic(),
                client.getBirthday(), client.getPhone(), client.getEmail(), client.getIsActive(),
                lockerId, now, now);

        client.setId(id);
        client.setCreatedDatetime(now);
        client.setUpdatedDatetime(now);
        return client;
    }

    public Client update(Client client) {
        log.debug("Обновление клиента с id: {}", client.getId());
        String sql = "UPDATE clients SET surname = ?, name = ?, patronymic = ?, birthday = ?, phone = ?, email = ?, " +
                "is_active = ?, locker_id = ?, trainer_id = ?, updated_datetime = ? WHERE id = ?";
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(sql, client.getSurname(), client.getName(), client.getPatronymic(),
                client.getBirthday(), client.getPhone(), client.getEmail(), client.getIsActive(),
                client.getLocker() != null ? client.getLocker().getId() : null,
                client.getTrainer() != null ? client.getTrainer().getId() : null,
                now, client.getId());

        client.setUpdatedDatetime(now);
        return client;
    }

    public Client findById(UUID id) {
        log.debug("Поиск клиента по id: {}", id);
        String sql = "SELECT * FROM clients WHERE id = ?";
        List<Client> clients = jdbcTemplate.query(sql, new ClientRowMapper(), id);
        return clients.isEmpty() ? null : clients.get(0);
    }

    public List<Client> findAll() {
        log.debug("Получение списка всех клиентов");
        String sql = "SELECT * FROM clients ORDER BY created_datetime DESC";
        return jdbcTemplate.query(sql, new ClientRowMapper());
    }

    public List<String> findClientNamesByTrainerId(UUID trainerId) {
        log.debug("Получение имен клиентов для тренера: {}", trainerId);
        String sql = "SELECT CONCAT(surname, ' ', name, ' ', COALESCE(patronymic, '')) as client_name FROM clients WHERE trainer_id = ? ORDER BY surname, name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("client_name"), trainerId);
    }

    public Client findClientDetailById(UUID id) {
        log.debug("Поиск детальной информации о клиенте по id: {}", id);
        String sql = """
                SELECT
                    c.*,

                    t.id as trainer_id,
                    t.surname as trainer_surname,
                    t.name as trainer_name,
                    t.patronymic as trainer_patronymic,
                    t.phone as trainer_phone,
                    t.status as trainer_status,
                    t.created_datetime as trainer_created_datetime,
                    t.updated_datetime as trainer_updated_datetime,

                    l.id as locker_id,
                    l.number as locker_number,
                    l.created_datetime as locker_created_datetime,
                    l.updated_datetime as locker_updated_datetime,

                    s.id as service_id,
                    s.name as service_name,
                    s.price as service_price,
                    s.created_datetime as service_created_datetime,
                    s.updated_datetime as service_updated_datetime
                FROM clients c
                LEFT JOIN trainers t ON c.trainer_id = t.id
                LEFT JOIN lockers l ON c.locker_id = l.id
                LEFT JOIN client_services cs ON c.id = cs.client_id
                LEFT JOIN services s ON cs.service_id = s.id
                WHERE c.id = ?
                ORDER BY s.name
                """;

        return jdbcTemplate.query(sql, new ClientDetailResultSetExtractor(), id);
    }

    private static class ClientDetailResultSetExtractor implements ResultSetExtractor<Client> {
        @Override
        public Client extractData(ResultSet rs) throws SQLException, DataAccessException {
            Client client = null;
            List<AdditionalService> services = new ArrayList<>();

            while (rs.next()) {
                if (client == null) {
                    client = mapClient(rs);

                    if (rs.getString("trainer_id") != null) {
                        client.setTrainer(mapTrainer(rs));
                    }

                    if (rs.getString("locker_id") != null) {
                        client.setLocker(mapLocker(rs));
                    }
                }

                if (rs.getString("service_id") != null) {
                    AdditionalService serviceInfo = new AdditionalService();
                    serviceInfo.setId(rs.getString("service_id"));
                    serviceInfo.setName(rs.getString("service_name"));
                    serviceInfo.setPrice(rs.getInt("service_price"));
                    serviceInfo.setCreatedDatetime(rs.getObject("service_created_datetime", LocalDateTime.class));
                    serviceInfo.setUpdatedDatetime(rs.getObject("service_updated_datetime", LocalDateTime.class));
                    services.add(serviceInfo);
                }
            }

            if (client != null) {
                client.setServices(services);
            }

            return client;
        }

        private Client mapClient(ResultSet rs) throws SQLException {
            Client client = new Client();
            client.setId(UUID.fromString(rs.getString("id")));
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

        private Trainer mapTrainer(ResultSet rs) throws SQLException {
            Trainer trainer = new Trainer();
            trainer.setId(UUID.fromString(rs.getString("trainer_id")));
            trainer.setSurname(rs.getString("trainer_surname"));
            trainer.setName(rs.getString("trainer_name"));
            trainer.setPatronymic(rs.getString("trainer_patronymic"));
            trainer.setPhone(rs.getString("trainer_phone"));
            trainer.setStatus(TrainerStatus.valueOf(rs.getString("trainer_status")));
            trainer.setCreatedDatetime(rs.getObject("trainer_created_datetime", LocalDateTime.class));
            trainer.setUpdatedDatetime(rs.getObject("trainer_updated_datetime", LocalDateTime.class));
            return trainer;
        }

        private Locker mapLocker(ResultSet rs) throws SQLException {
            Locker locker = new Locker();
            locker.setId(UUID.fromString(rs.getString("locker_id")));
            locker.setNumber(rs.getInt("locker_number"));
            locker.setCreatedDatetime(rs.getObject("locker_created_datetime", LocalDateTime.class));
            locker.setUpdatedDatetime(rs.getObject("locker_updated_datetime", LocalDateTime.class));
            return locker;
        }
    }
}
