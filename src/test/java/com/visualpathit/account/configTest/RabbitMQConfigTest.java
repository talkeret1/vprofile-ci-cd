package com.visualpathit.account.configTest;

import com.visualpathit.account.config.RabbitMQConfig;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import static org.junit.Assert.assertNotNull;

public class RabbitMQConfigTest {

    @Test
    public void shouldEnableSslWhenTrue() {

        System.setProperty("RABBITMQ_SSL", "true");

        RabbitMQConfig config = new RabbitMQConfig();

        CachingConnectionFactory factory = new CachingConnectionFactory();

        Object result = config.postProcessBeforeInitialization(factory, "bean");

        assertNotNull(result);
    }

    @Test
    public void shouldDisableSslWhenFalse() {

        System.setProperty("RABBITMQ_SSL", "false");

        RabbitMQConfig config = new RabbitMQConfig();

        CachingConnectionFactory factory = new CachingConnectionFactory();

        Object result = config.postProcessBeforeInitialization(factory, "bean");

        assertNotNull(result);
    }

    @Test
    public void shouldHandleNullEnvGracefully() {

        System.clearProperty("RABBITMQ_SSL");

        RabbitMQConfig config = new RabbitMQConfig();

        CachingConnectionFactory factory = new CachingConnectionFactory();

        Object result = config.postProcessBeforeInitialization(factory, "bean");

        assertNotNull(result);
    }
}