package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RandomNumberGeneratorTest {

    @InjectMocks private RandomNumberGenerator randomNumberGenerator;

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 6, 9})
    public void testGetIntRandomNumberInRange_Success(int digit) {
        String result = randomNumberGenerator.getIntRandomNumberInRange(digit);
        assertEquals(digit, result.length());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 11})
    public void testGetIntRandomNumberInRange_default(int digit) {
        String result = randomNumberGenerator.getIntRandomNumberInRange(digit);
        assertEquals(6, result.length());
    }
}
