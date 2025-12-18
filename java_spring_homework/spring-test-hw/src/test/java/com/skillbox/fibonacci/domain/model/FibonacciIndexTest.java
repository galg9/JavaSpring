package com.skillbox.fibonacci.domain.model;

import com.skillbox.fibonacci.domain.model.exception.InvalidFibonacciIndexException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciIndexTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, -100})
    void shouldThrowExceptionWhenIndexIsLessThanOne(int invalidIndex) {
        // when & then
        assertThrows(InvalidFibonacciIndexException.class, () -> new FibonacciIndex(invalidIndex));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 47, 100})
    void shouldCreateIndexWithValidValue(int validIndex) {
        // when
        FibonacciIndex index = new FibonacciIndex(validIndex);

        // then
        assertEquals(validIndex, index.n());
    }
}
