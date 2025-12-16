package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AdditionalServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class AdditionalServiceRowMapper implements RowMapper<AdditionalService> {
        @Override
        public AdditionalService mapRow(ResultSet rs, int rowNum) throws SQLException {
            AdditionalService additionalService = new AdditionalService();
            additionalService.setId(rs.getString("id"));
            additionalService.setName(rs.getString("name"));
            additionalService.setPrice(rs.getInt("price"));
            additionalService.setCreatedDatetime(rs.getObject("created_datetime", LocalDateTime.class));
            additionalService.setUpdatedDatetime(rs.getObject("updated_datetime", LocalDateTime.class));
            return additionalService;
        }
    }

    public AdditionalService findById(String id) {
        log.debug("Поиск услуги по id: {}", id);
        String sql = "SELECT * FROM services WHERE id = ?";
        List<AdditionalService> services = jdbcTemplate.query(sql, new AdditionalServiceRowMapper(), id);
        return services.isEmpty() ? null : services.get(0);
    }

    public List<AdditionalService> findAll() {
        log.debug("Получение списка всех услуг");
        String sql = "SELECT * FROM services ORDER BY name";
        return jdbcTemplate.query(sql, new AdditionalServiceRowMapper());
    }

    public void addServiceToClient(UUID clientId, String serviceId) {
        log.debug("Добавление услуги {} клиенту {}", serviceId, clientId);
        String sql = "INSERT INTO client_services (client_id, service_id) VALUES (?, ?) ON CONFLICT (client_id, service_id) DO NOTHING";
        jdbcTemplate.update(sql, clientId, serviceId);
    }

    public List<String> findClientNamesByServiceId(String serviceId) {
        log.debug("Получение имен клиентов для услуги: {}", serviceId);
        String sql = "SELECT CONCAT(c.surname, ' ', c.name, ' ', COALESCE(c.patronymic, '')) as client_name " +
                "FROM clients c " +
                "INNER JOIN client_services cs ON c.id = cs.client_id " +
                "WHERE cs.service_id = ? ORDER BY client_name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("client_name"), serviceId);
    }
}