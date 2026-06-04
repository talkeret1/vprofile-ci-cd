package com.visualpathit.account.configTest;

import com.visualpathit.account.config.RabbitMQConfig;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import static org.junit.Assert.*;

public class RabbitMQConfigTest {

    private void clearSsl() {
        System.clearProperty("RABBITMQ_SSL");
    }

    @Test
    public void shouldEnableSslWhenTrue() {

        System.setProperty("RABBITMQ_SSL", "true");

        RabbitMQConfig config = new RabbitMQConfig();
        CachingConnectionFactory factory = new CachingConnectionFactory();

        Object result = config.postProcessBeforeInitialization(factory, "bean");

        assertNotNull(result);

        clearSsl();
    }

    @Test
    public void shouldDisableSslWhenFalse() {

        System.setProperty("RABBITMQ_SSL", "false");

        RabbitMQConfig config = new RabbitMQConfig();
        CachingConnectionFactory factory = new CachingConnectionFactory();

        Object result = config.postProcessBeforeInitialization(factory, "bean");

        assertNotNull(result);

        clearSsl();
    }

    @Test
    public void shouldHandleNullProperty() {

        clearSsl();

        RabbitMQConfig config = new RabbitMQConfig();
        CachingConnectionFactory factory = new CachingConnectionFactory();

        Object result = config.postProcessBeforeInitialization(factory, "bean");

        assertNotNull(result);
    }

    @Test
    public void shouldIgnoreNonCachingFactory() {

        RabbitMQConfig config = new RabbitMQConfig();

        Object bean = new Object();

        Object result = config.postProcessBeforeInitialization(bean, "bean");

        assertSame(bean, result);
    }

    @Test
    public void shouldCreateConnectionFactoryWithoutThrowing() {

        RabbitMQConfig config = new RabbitMQConfig();

        CachingConnectionFactory factory = config.connectionFactory();

        assertNotNull(factory);
        assertEquals("localhost", factory.getHost());
        assertEquals(5672, factory.getPort());
    }
}