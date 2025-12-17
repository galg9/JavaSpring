package ru.skillbox.skillfitbox.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class ClientRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Client save(Client client) {
        log.info("Сохранение нового клиента: {}", client.getSurname());
        LocalDateTime now = LocalDateTime.now();
        client.setCreatedDatetime(now);
        client.setUpdatedDatetime(now);
        entityManager.persist(client);
        log.info("Клиент успешно сохранен с ID: {}", client.getId());
        return client;
    }

    public Client update(Client client) {
        log.info("Обновление клиента с ID: {}", client.getId());
        client.setUpdatedDatetime(LocalDateTime.now());
        Client updated = entityManager.merge(client);
        log.info("Клиент успешно обновлен");
        return updated;
    }

    public Client findById(UUID id) {
        log.info("Поиск клиента по ID: {}", id);
        Client client = entityManager.find(Client.class, id);
        if (client != null) {
            log.info("Клиент найден: {}", client.getSurname());
        } else {
            log.warn("Клиент с ID {} не найден", id);
        }
        return client;
    }

    public List<Client> findAll() {
        log.info("Получение списка всех клиентов");
        TypedQuery<Client> query = entityManager.createQuery(
                "SELECT c FROM Client c ORDER BY c.createdDatetime DESC", Client.class);
        List<Client> clients = query.getResultList();
        log.info("Найдено клиентов: {}", clients.size());
        return clients;
    }

    public Client findClientDetailById(UUID id) {
        log.info("Получение детальной информации о клиенте с ID: {}", id);
        TypedQuery<Client> query = entityManager.createQuery(
                "SELECT c FROM Client c " +
                        "LEFT JOIN FETCH c.trainer " +
                        "LEFT JOIN FETCH c.locker " +
                        "LEFT JOIN FETCH c.services " +
                        "WHERE c.id = :id", Client.class);
        query.setParameter("id", id);
        List<Client> results = query.getResultList();
        Client client = results.isEmpty() ? null : results.get(0);
        if (client != null) {
            log.info("Детальная информация о клиенте получена: {}", client.getSurname());
        } else {
            log.warn("Клиент с ID {} не найден", id);
        }
        return client;
    }
}
