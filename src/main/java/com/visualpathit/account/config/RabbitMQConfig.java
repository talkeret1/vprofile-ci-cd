package com.visualpathit.account.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig implements BeanPostProcessor {

    @Override
    public @NonNull Object postProcessBeforeInitialization(
            @NonNull Object bean,
            @NonNull String beanName) {

        if (bean instanceof CachingConnectionFactory cachingFactory) {

            String sslEnabled = System.getProperty("RABBITMQ_SSL");

            if ("true".equalsIgnoreCase(sslEnabled)) {
                try {
                    ConnectionFactory rabbitFactory = cachingFactory.getRabbitConnectionFactory();
                    rabbitFactory.useSslProtocol();
                    System.out.println("RabbitMQ SSL ENABLED");

                } catch (Exception e) {
                    throw new RuntimeException("Failed to enable SSL", e);
                }
            } else {
                System.out.println("RabbitMQ SSL DISABLED");
            }
        }

        return bean;
    }

    public CachingConnectionFactory connectionFactory() {

        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        return factory;
    }
}