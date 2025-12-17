package ru.skillbox.skillfitbox.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Trainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class TrainerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Trainer save(Trainer trainer) {
        log.info("Сохранение нового тренера: {}", trainer.getSurname());
        LocalDateTime now = LocalDateTime.now();
        trainer.setCreatedDatetime(now);
        trainer.setUpdatedDatetime(now);
        entityManager.persist(trainer);
        log.info("Тренер успешно сохранен с ID: {}", trainer.getId());
        return trainer;
    }

    public Trainer update(Trainer trainer) {
        log.info("Обновление тренера с ID: {}", trainer.getId());
        trainer.setUpdatedDatetime(LocalDateTime.now());
        Trainer updated = entityManager.merge(trainer);
        log.info("Тренер успешно обновлен");
        return updated;
    }

    public Trainer findById(UUID id) {
        log.info("Поиск тренера по ID: {}", id);
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer != null) {
            log.info("Тренер найден: {}", trainer.getSurname());
        } else {
            log.warn("Тренер с ID {} не найден", id);
        }
        return trainer;
    }

    public List<Trainer> findAll() {
        log.info("Получение списка всех тренеров");
        TypedQuery<Trainer> query = entityManager.createQuery(
                "SELECT t FROM Trainer t ORDER BY t.createdDatetime DESC", Trainer.class);
        List<Trainer> trainers = query.getResultList();
        log.info("Найдено тренеров: {}", trainers.size());
        return trainers;
    }

    public Trainer findTrainerDetailById(UUID id) {
        log.info("Получение детальной информации о тренере с ID: {}", id);
        TypedQuery<Trainer> query = entityManager.createQuery(
                "SELECT t FROM Trainer t " +
                        "LEFT JOIN FETCH t.clients " +
                        "WHERE t.id = :id", Trainer.class);
        query.setParameter("id", id);
        List<Trainer> results = query.getResultList();
        Trainer trainer = results.isEmpty() ? null : results.get(0);
        if (trainer != null) {
            log.info("Детальная информация о тренере получена: {}", trainer.getSurname());
        } else {
            log.warn("Тренер с ID {} не найден", id);
        }
        return trainer;
    }
}
