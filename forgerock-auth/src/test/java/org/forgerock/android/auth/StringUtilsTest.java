package org.forgerock.android.auth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void testIsNotEmpty() {
        assertTrue(StringUtils.isNotEmpty("test"));
        assertFalse(StringUtils.isNotEmpty(""));
        assertFalse(StringUtils.isNotEmpty(null));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(StringUtils.isEmpty("test"));
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(null));
    }
}
