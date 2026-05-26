package com.visualpathit.account.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        if (bean instanceof CachingConnectionFactory cachingFactory) {

            String sslEnabled = System.getenv("RABBITMQ_SSL");

            if ("true".equalsIgnoreCase(sslEnabled)) {
                try {

                    ConnectionFactory rabbitFactory = cachingFactory.getRabbitConnectionFactory();

                    rabbitFactory.useSslProtocol();

                    System.out.println("RabbitMQ SSL ENABLED");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("RabbitMQ SSL DISABLED");
            }
        }

        return bean;
    }
}