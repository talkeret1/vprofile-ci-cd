package com.visualpathit.account.utilsTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.utils.RabbitMqUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RabbitMqUtilTest {

    @BeforeEach
    void reset() {
        RabbitMqUtil.clear();
    }

    // -------------------------
    // NULL OBJECT SCENARIO
    // -------------------------

    @Test
    void shouldReturnNullWhenNotConfigured() {

        assertNull(RabbitMqUtil.getRabbitMqHost());
        assertNull(RabbitMqUtil.getRabbitMqPort());
        assertNull(RabbitMqUtil.getRabbitMqUser());
        assertNull(RabbitMqUtil.getRabbitMqPassword());
        assertFalse(RabbitMqUtil.isConfigured());
    }

    // -------------------------
    // CONFIGURED SCENARIO
    // -------------------------

    @Test
    void shouldReturnAllConfiguredValues() {

        Components components = mock(Components.class);

        when(components.getRabbitMqHost()).thenReturn("localhost");
        when(components.getRabbitMqPort()).thenReturn("5672");
        when(components.getRabbitMqUser()).thenReturn("guest");
        when(components.getRabbitMqPassword()).thenReturn("guest");

        RabbitMqUtil.setComponents(components);

        assertEquals("localhost", RabbitMqUtil.getRabbitMqHost());
        assertEquals("5672", RabbitMqUtil.getRabbitMqPort());
        assertEquals("guest", RabbitMqUtil.getRabbitMqUser());
        assertEquals("guest", RabbitMqUtil.getRabbitMqPassword());
        assertTrue(RabbitMqUtil.isConfigured());
    }

    // -------------------------
    // REPLACEMENT SCENARIO
    // -------------------------

    @Test
    void shouldReplaceComponents() {

        Components first = mock(Components.class);
        Components second = mock(Components.class);

        when(first.getRabbitMqHost()).thenReturn("host1");
        when(second.getRabbitMqHost()).thenReturn("host2");

        RabbitMqUtil.setComponents(first);
        assertEquals("host1", RabbitMqUtil.getRabbitMqHost());

        RabbitMqUtil.setComponents(second);
        assertEquals("host2", RabbitMqUtil.getRabbitMqHost());
    }

    // -------------------------
    // CLEAR SCENARIO
    // -------------------------

    @Test
    void shouldClearConfiguration() {

        Components components = mock(Components.class);
        when(components.getRabbitMqHost()).thenReturn("localhost");

        RabbitMqUtil.setComponents(components);
        assertTrue(RabbitMqUtil.isConfigured());

        RabbitMqUtil.clear();

        assertFalse(RabbitMqUtil.isConfigured());
        assertNull(RabbitMqUtil.getRabbitMqHost());
    }
}