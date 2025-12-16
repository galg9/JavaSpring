package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Trainer;
import ru.skillbox.skillfitbox.entity.TrainerStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TrainerRepository {

    private final JdbcTemplate jdbcTemplate;

    private static class TrainerRowMapper implements RowMapper<Trainer> {
        @Override
        public Trainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Trainer trainer = new Trainer();
            trainer.setId(UUID.fromString(rs.getString("id")));
            trainer.setSurname(rs.getString("surname"));
            trainer.setName(rs.getString("name"));
            trainer.setPatronymic(rs.getString("patronymic"));
            trainer.setPhone(rs.getString("phone"));
            trainer.setStatus(TrainerStatus.valueOf(rs.getString("status")));
            trainer.setCreatedDatetime(rs.getObject("created_datetime", LocalDateTime.class));
            trainer.setUpdatedDatetime(rs.getObject("updated_datetime", LocalDateTime.class));
            return trainer;
        }
    }

    public Trainer save(Trainer trainer) {
        log.debug("Сохранение тренера: {} {}", trainer.getSurname(), trainer.getName());
        String sql = "INSERT INTO trainers (id, surname, name, patronymic, phone, status, created_datetime, updated_datetime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        UUID id = trainer.getId() != null ? trainer.getId() : UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(sql, id, trainer.getSurname(), trainer.getName(),
                trainer.getPatronymic(), trainer.getPhone(), trainer.getStatus().name(), now, now);

        trainer.setId(id);
        trainer.setCreatedDatetime(now);
        trainer.setUpdatedDatetime(now);
        return trainer;
    }

    public Trainer update(Trainer trainer) {
        log.debug("Обновление тренера с id: {}", trainer.getId());
        String sql = "UPDATE trainers SET surname = ?, name = ?, patronymic = ?, phone = ?, status = ?, updated_datetime = ? WHERE id = ?";
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(sql, trainer.getSurname(), trainer.getName(),
                trainer.getPatronymic(), trainer.getPhone(), trainer.getStatus().name(), now, trainer.getId());

        trainer.setUpdatedDatetime(now);
        return trainer;
    }

    public Trainer findById(UUID id) {
        log.debug("Поиск тренера по id: {}", id);
        String sql = "SELECT * FROM trainers WHERE id = ?";
        List<Trainer> trainers = jdbcTemplate.query(sql, new TrainerRowMapper(), id);
        return trainers.isEmpty() ? null : trainers.get(0);
    }

    public List<Trainer> findAll() {
        log.debug("Получение списка всех тренеров");
        String sql = "SELECT * FROM trainers ORDER BY created_datetime DESC";
        return jdbcTemplate.query(sql, new TrainerRowMapper());
    }
}