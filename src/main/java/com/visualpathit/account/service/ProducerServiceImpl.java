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
        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            channel.basicPublish(
                    EXCHANGE_NAME,
                    "",
                    null,
                    message == null ? new byte[0] : message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");

        } catch (IOException | TimeoutException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } finally {
            try {
                if (channel != null)
                    channel.close();
                if (connection != null)
                    connection.close();
            } catch (Exception ignored) {
            }
        }

        return "response";
    }
}