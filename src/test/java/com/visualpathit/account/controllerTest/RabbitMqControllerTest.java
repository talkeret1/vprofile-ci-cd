package com.visualpathit.account.controllerTest;

import com.visualpathit.account.controller.RabbitMqController;
import com.visualpathit.account.beans.Components;
import com.visualpathit.account.utils.RabbitMqUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RabbitMqControllerTest {

    private RabbitMqController controller;

    @Before
    public void setup() throws Exception {

        Components components = Mockito.mock(Components.class);

        Mockito.when(components.getRabbitMqHost()).thenReturn("localhost");
        Mockito.when(components.getRabbitMqPort()).thenReturn("5672");
        Mockito.when(components.getRabbitMqUser()).thenReturn("guest");
        Mockito.when(components.getRabbitMqPassword()).thenReturn("guest");

        setStaticComponents(components);

        controller = new RabbitMqController();
    }

    @Test
    public void shouldReturnErrorViewWhenConnectionFails() {

        ModelAndView result = controller.checkRabbitMqStatus();

        assertEquals("rabbitmq-error", result.getViewName());
    }

    @Test
    public void shouldReturnModelAndViewNotNull() {
        ModelAndView result = controller.checkRabbitMqStatus();
        assertNotNull(result);
    }

    private void setStaticComponents(Components value) throws Exception {

        Field field = RabbitMqUtil.class.getDeclaredField("object");
        field.setAccessible(true);

        field.set(null, value);
    }
}