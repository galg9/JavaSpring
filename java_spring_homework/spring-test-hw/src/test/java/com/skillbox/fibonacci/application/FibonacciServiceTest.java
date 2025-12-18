package com.skillbox.fibonacci.application;

import com.skillbox.fibonacci.adapter.persistence.FibonacciRepository;
import com.skillbox.fibonacci.adapter.persistence.entity.FibonacciNumberEntity;
import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import com.skillbox.fibonacci.domain.model.FibonacciNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FibonacciServiceTest {

    @Mock
    private FibonacciRepository repository;

    @Mock
    private RecursiveFibonacciCalculator calculator;

    @InjectMocks
    private FibonacciService service;

    @Test
    void shouldReturnFibonacciNumberFromDatabaseWhenExists() {
        // given
        FibonacciIndex index = new FibonacciIndex(5);
        FibonacciNumberEntity entity = new FibonacciNumberEntity(5, 5L);
        when(repository.findByIndex(5)).thenReturn(Optional.of(entity));

        // when
        FibonacciNumber result = service.fibonacciNumber(index);

        // then
        assertEquals(5, result.n().n());
        assertEquals(BigInteger.valueOf(5), result.value());
        
        verify(repository, times(1)).findByIndex(5);
        verify(calculator, never()).getFibonacciNumber(any());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldCalculateAndSaveFibonacciNumberWhenNotInDatabase() {
        // given
        FibonacciIndex index = new FibonacciIndex(7);
        BigInteger expectedValue = BigInteger.valueOf(13);
        
        when(repository.findByIndex(7)).thenReturn(Optional.empty());
        when(calculator.getFibonacciNumber(index)).thenReturn(expectedValue);

        // when
        FibonacciNumber result = service.fibonacciNumber(index);

        // then
        assertEquals(7, result.n().n());
        assertEquals(expectedValue, result.value());
        
        verify(repository, times(1)).findByIndex(7);
        verify(calculator, times(1)).getFibonacciNumber(index);
        verify(repository, times(1)).save(argThat(entity ->
                entity.getIndex() == 7 && entity.getValue() == 13L
        ));
    }
}
