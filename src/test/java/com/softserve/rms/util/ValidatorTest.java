package com.softserve.rms.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {

    @InjectMocks
    private Validator validator;

    @Test
    public void testGenerationOfTableOrColumnName() {
        String string = "Any-string/to:be;generated!";
        String result = "any_string_to_be_generated_";
        assertEquals(result, validator.generateTableOrColumnName(string));
    }
}