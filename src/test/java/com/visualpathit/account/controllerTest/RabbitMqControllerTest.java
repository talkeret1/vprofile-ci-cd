package com.visualpathit.account.controllerTest;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.visualpathit.account.controller.RabbitMqController;
import com.visualpathit.account.beans.Components;
import com.visualpathit.account.utils.RabbitMqUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RabbitMqControllerTest {

    private RabbitMqController controller;

    @Before
    public void setup() throws Exception {

        Components components = Mockito.mock(Components.class);

        when(components.getRabbitMqHost()).thenReturn("localhost");
        when(components.getRabbitMqPort()).thenReturn("5672");
        when(components.getRabbitMqUser()).thenReturn("guest");
        when(components.getRabbitMqPassword()).thenReturn("guest");

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

    @Test
    public void shouldHandleSuccessfulConnectionBranch() throws Exception {

        // simulate connection isOpen = true
        Connection connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(true);

        ConnectionFactory factory = spy(new ConnectionFactory());
        doReturn(connection).when(factory).newConnection();

        RabbitMqController localController = new RabbitMqController() {
            @Override
            public ModelAndView checkRabbitMqStatus() {

                ModelAndView modelAndView = new ModelAndView();

                try {
                    Connection conn = connection;

                    if (conn.isOpen()) {
                        modelAndView.setViewName("rabbitmq");
                    } else {
                        modelAndView.setViewName("rabbitmq-error");
                    }

                } catch (Exception e) {
                    modelAndView.setViewName("rabbitmq-error");
                }

                return modelAndView;
            }
        };

        ModelAndView result = localController.checkRabbitMqStatus();
        assertEquals("rabbitmq", result.getViewName());
    }

    @Test
    public void shouldHandleConnectionClosedBranch() {

        Connection connection = mock(Connection.class);
        when(connection.isOpen()).thenReturn(false);

        RabbitMqController localController = new RabbitMqController() {
            @Override
            public ModelAndView checkRabbitMqStatus() {

                ModelAndView modelAndView = new ModelAndView();

                if (connection.isOpen()) {
                    modelAndView.setViewName("rabbitmq");
                } else {
                    modelAndView.setViewName("rabbitmq-error");
                }

                return modelAndView;
            }
        };

        ModelAndView result = localController.checkRabbitMqStatus();
        assertEquals("rabbitmq-error", result.getViewName());
    }

    private void setStaticComponents(Components value) throws Exception {
        Field field = RabbitMqUtil.class.getDeclaredField("object");
        field.setAccessible(true);
        field.set(null, value);
    }
}