package com.visualpathit.account.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.visualpathit.account.utils.RabbitMqUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class ProducerServiceImpl implements ProducerService {

    private static final String EXCHANGE_NAME = "messages";

    private final ConnectionFactory connectionFactory;

    public ProducerServiceImpl() {
        this.connectionFactory = createFactory();
    }

    public ProducerServiceImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private ConnectionFactory createFactory() {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(RabbitMqUtil.getRabbitMqHost());
        factory.setPort(Integer.parseInt(RabbitMqUtil.getRabbitMqPort()));
        factory.setUsername(RabbitMqUtil.getRabbitMqUser());
        factory.setPassword(RabbitMqUtil.getRabbitMqPassword());

        return factory;
    }

    @Override
    public String produceMessage(String message) {
        try (Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.basicPublish(EXCHANGE_NAME, "", null,
                    message == null ? "".getBytes() : message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }

        return "response";
    }
}