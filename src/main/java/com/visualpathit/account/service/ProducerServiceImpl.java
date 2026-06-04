package com.visualpathit.account.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.visualpathit.account.utils.RabbitMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class ProducerServiceImpl implements ProducerService {

    private static final Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

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

            if (connection == null) {
                logger.warn("RabbitMQ connection is null");
                return "response";
            }

            channel = connection.createChannel();

            if (channel == null) {
                logger.warn("RabbitMQ channel is null");
                return "response";
            }

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            byte[] msgBytes = (message == null) ? "".getBytes() : message.getBytes();

            channel.basicPublish(EXCHANGE_NAME, "", null, msgBytes);

            logger.info("RabbitMQ message sent: {}", message);

        } catch (IOException | TimeoutException e) {
            logger.error("RabbitMQ error while sending message", e);

        } catch (Exception e) {
            logger.error("Unexpected RabbitMQ error", e);

        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
            } catch (Exception ignored) {
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception ignored) {
            }
        }

        return "response";
    }
}