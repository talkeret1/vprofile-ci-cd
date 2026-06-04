package com.visualpathit.account.utilsTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.utils.RabbitMqUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RabbitMqUtilTest {

    private Components components;

    @BeforeEach
    void setUp() throws Exception {

        java.lang.reflect.Field field = RabbitMqUtil.class.getDeclaredField("object");
        field.setAccessible(true);
        field.set(null, null);

        components = mock(Components.class);

        when(components.getRabbitMqHost()).thenReturn("localhost");
        when(components.getRabbitMqPort()).thenReturn("5672");
        when(components.getRabbitMqUser()).thenReturn("guest");
        when(components.getRabbitMqPassword()).thenReturn("guest");

        RabbitMqUtil util = new RabbitMqUtil();
        util.setComponents(components);
    }

    @Test
    void shouldReturnRabbitMqConfigurationValues() {

        assertEquals("localhost", RabbitMqUtil.getRabbitMqHost());
        assertEquals("5672", RabbitMqUtil.getRabbitMqPort());
        assertEquals("guest", RabbitMqUtil.getRabbitMqUser());
        assertEquals("guest", RabbitMqUtil.getRabbitMqPassword());

        verify(components).getRabbitMqHost();
    }

    @Test
    void shouldReplaceStaticComponentsReference() {

        Components second = mock(Components.class);

        when(second.getRabbitMqHost()).thenReturn("host2");
        when(second.getRabbitMqPort()).thenReturn("5672");
        when(second.getRabbitMqUser()).thenReturn("u2");
        when(second.getRabbitMqPassword()).thenReturn("p2");

        new RabbitMqUtil().setComponents(second);

        assertEquals("host2", RabbitMqUtil.getRabbitMqHost());
    }

    @Test
    void shouldReturnNullWhenComponentsNotSet() throws Exception {

        java.lang.reflect.Field field = RabbitMqUtil.class.getDeclaredField("object");
        field.setAccessible(true);
        field.set(null, null);

        assertNull(RabbitMqUtil.getRabbitMqHost());
    }
}