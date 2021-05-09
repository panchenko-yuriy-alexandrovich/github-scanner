package app.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

    StringUtils stringUtils = new StringUtils();

    @Test
    void isEmpty_whenStringIsNull_thenReturnTrue() {

        assertTrue(stringUtils.isEmpty(null));
    }

    @Test
    void isEmpty_whenStringIsEmpty_thenReturnTrue() {

        assertTrue(stringUtils.isEmpty(""));
    }

    @Test
    void isEmpty_whenStringIsNotEmpty_thenReturnFalse() {

        assertFalse(stringUtils.isEmpty("test"));
    }
}