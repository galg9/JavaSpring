package ru.skillbox.skillfitbox.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;

import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class AdditionalServiceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public AdditionalService findById(String id) {
        log.info("Поиск услуги по ID: {}", id);
        AdditionalService service = entityManager.find(AdditionalService.class, id);
        if (service != null) {
            log.info("Услуга найдена: {}", service.getName());
        } else {
            log.warn("Услуга с ID {} не найдена", id);
        }
        return service;
    }

    public List<AdditionalService> findAllWithDetails() {
        log.info("Получение списка всех услуг с детальной информацией");
        TypedQuery<AdditionalService> query = entityManager.createQuery(
                "SELECT DISTINCT s FROM AdditionalService s " +
                        "LEFT JOIN FETCH s.clients " +
                        "ORDER BY s.name", AdditionalService.class);
        List<AdditionalService> services = query.getResultList();
        log.info("Найдено услуг: {}", services.size());
        return services;
    }

    public AdditionalService findDetailsById(String id) {
        log.info("Получение детальной информации об услуге с ID: {}", id);
        TypedQuery<AdditionalService> query = entityManager.createQuery(
                "SELECT s FROM AdditionalService s " +
                        "LEFT JOIN FETCH s.clients " +
                        "WHERE s.id = :id", AdditionalService.class);
        query.setParameter("id", id);
        List<AdditionalService> results = query.getResultList();
        AdditionalService service = results.isEmpty() ? null : results.get(0);
        if (service != null) {
            log.info("Детальная информация об услуге получена: {}", service.getName());
        } else {
            log.warn("Услуга с ID {} не найдена", id);
        }
        return service;
    }

    public void addServiceToClient(UUID clientId, String serviceId) {
        log.info("Добавление услуги {} клиенту {}", serviceId, clientId);
        // Используем нативный запрос для связующей таблицы
        entityManager.createNativeQuery(
                        "INSERT INTO client_services (client_id, service_id) VALUES (:clientId, :serviceId) " +
                                "ON CONFLICT (client_id, service_id) DO NOTHING")
                .setParameter("clientId", clientId)
                .setParameter("serviceId", serviceId)
                .executeUpdate();
        log.info("Услуга успешно добавлена клиенту");
    }
}
