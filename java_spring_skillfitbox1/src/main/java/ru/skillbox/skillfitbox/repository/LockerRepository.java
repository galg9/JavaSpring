package ru.skillbox.skillfitbox.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Locker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class LockerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Locker findById(UUID id) {
        log.info("Поиск шкафчика по ID: {}", id);
        TypedQuery<Locker> query = entityManager.createQuery(
                "SELECT l FROM Locker l LEFT JOIN FETCH l.client WHERE l.id = :id", Locker.class);
        query.setParameter("id", id);
        List<Locker> results = query.getResultList();
        Locker locker = results.isEmpty() ? null : results.get(0);
        if (locker != null) {
            log.info("Шкафчик найден: {}", locker.getNumber());
        } else {
            log.warn("Шкафчик с ID {} не найден", id);
        }
        return locker;
    }

    public List<Locker> findAllWithClientInfo() {
        log.info("Получение списка всех шкафчиков с информацией о клиентах");
        TypedQuery<Locker> query = entityManager.createQuery(
                "SELECT l FROM Locker l LEFT JOIN FETCH l.client ORDER BY l.number", Locker.class);
        List<Locker> lockers = query.getResultList();
        log.info("Найдено шкафчиков: {}", lockers.size());
        return lockers;
    }

    public void update(Locker locker) {
        log.info("Обновление шкафчика с ID: {}", locker.getId());
        locker.setUpdatedDatetime(LocalDateTime.now());
        entityManager.merge(locker);
        log.info("Шкафчик успешно обновлен");
    }
}
