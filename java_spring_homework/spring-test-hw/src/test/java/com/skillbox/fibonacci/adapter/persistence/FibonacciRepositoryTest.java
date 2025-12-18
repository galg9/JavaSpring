package com.skillbox.fibonacci.adapter.persistence;

import com.skillbox.fibonacci.PostgresTestContainerInitializer;
import com.skillbox.fibonacci.adapter.persistence.entity.FibonacciNumberEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PostgresTestContainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FibonacciRepositoryTest {

    @Autowired
    private FibonacciRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveNewFibonacciNumber() {
        // given
        FibonacciNumberEntity entity = new FibonacciNumberEntity(5, 5L);

        // when
        FibonacciNumberEntity saved = repository.save(entity);
        entityManager.flush();
        entityManager.detach(saved);

        // then
        assertNotNull(saved.getId());
        assertEquals(5, saved.getIndex());
        assertEquals(5L, saved.getValue());
    }

    @Test
    void shouldFindFibonacciNumberByIndex() {
        // given
        FibonacciNumberEntity entity = new FibonacciNumberEntity(7, 13L);
        repository.save(entity);
        entityManager.flush();

        // when
        Optional<FibonacciNumberEntity> found = repository.findByIndex(7);

        // then
        assertTrue(found.isPresent());
        assertEquals(7, found.get().getIndex());
        assertEquals(13L, found.get().getValue());
    }

    @Test
    void shouldNotCreateDuplicatesWhenSavingSameIndex() {
        // given
        FibonacciNumberEntity entity1 = new FibonacciNumberEntity(10, 55L);
        repository.save(entity1);
        entityManager.flush();
        entityManager.detach(entity1);

        // when
        FibonacciNumberEntity entity2 = new FibonacciNumberEntity(10, 55L);
        FibonacciNumberEntity saved = repository.save(entity2);
        entityManager.flush();
        entityManager.detach(saved);

        // then
        Optional<FibonacciNumberEntity> found = repository.findByIndex(10);
        assertTrue(found.isPresent());
        assertEquals(10, found.get().getIndex());
        assertEquals(55L, found.get().getValue());
    }
}
