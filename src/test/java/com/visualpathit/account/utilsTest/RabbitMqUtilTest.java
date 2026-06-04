package com.visualpathit.account.utilsTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.utils.RabbitMqUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RabbitMqUtilTest {

    @Test
    void shouldReturnRabbitMqConfigurationValues() {

        Components components = mock(Components.class);

        when(components.getRabbitMqHost()).thenReturn("localhost");
        when(components.getRabbitMqPort()).thenReturn("5672");
        when(components.getRabbitMqUser()).thenReturn("guest");
        when(components.getRabbitMqPassword()).thenReturn("guest");

        new RabbitMqUtil().setComponents(components);

        assertEquals("localhost", RabbitMqUtil.getRabbitMqHost());
        assertEquals("5672", RabbitMqUtil.getRabbitMqPort());
        assertEquals("guest", RabbitMqUtil.getRabbitMqUser());
        assertEquals("guest", RabbitMqUtil.getRabbitMqPassword());
    }

    @Test
    void shouldAllowComponentReplacement() {

        Components first = mock(Components.class);
        Components second = mock(Components.class);

        when(first.getRabbitMqHost()).thenReturn("host1");
        when(second.getRabbitMqHost()).thenReturn("host2");

        RabbitMqUtil util = new RabbitMqUtil();

        util.setComponents(first);
        assertEquals("host1", RabbitMqUtil.getRabbitMqHost());

        util.setComponents(second);
        assertEquals("host2", RabbitMqUtil.getRabbitMqHost());
    }
}