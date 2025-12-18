package com.skillbox.fibonacci.application;

import com.skillbox.fibonacci.domain.model.FibonacciIndex;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecursiveFibonacciCalculatorTest {

    private final RecursiveFibonacciCalculator calculator = new RecursiveFibonacciCalculator();

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 1",
            "3, 2",
            "4, 3",
            "5, 5",
            "6, 8",
            "7, 13",
            "8, 21",
            "9, 34",
            "10, 55",
            "12, 144",
            "30, 832040"
    })
    void shouldReturnCorrectFibonacciNumber(int index, long expectedValue) {
        // given
        FibonacciIndex fibonacciIndex = new FibonacciIndex(index);

        // when
        BigInteger result = calculator.getFibonacciNumber(fibonacciIndex);

        // then
        assertEquals(BigInteger.valueOf(expectedValue), result);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 1"
    })
    void shouldReturnOneForIndexOneAndTwo(int index, long expectedValue) {
        // given
        FibonacciIndex fibonacciIndex = new FibonacciIndex(index);

        // when
        BigInteger result = calculator.getFibonacciNumber(fibonacciIndex);

        // then
        assertEquals(BigInteger.valueOf(expectedValue), result);
    }
}
