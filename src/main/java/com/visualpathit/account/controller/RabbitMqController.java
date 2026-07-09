package com.visualpathit.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.visualpathit.account.utils.RabbitMqUtil;

@Controller
public class RabbitMqController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqController.class);

    @GetMapping("/user/rabbit")
    public ModelAndView checkRabbitMqStatus() {

        ModelAndView modelAndView = new ModelAndView();

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(RabbitMqUtil.getRabbitMqHost());
        factory.setPort(Integer.parseInt(RabbitMqUtil.getRabbitMqPort()));
        factory.setUsername(RabbitMqUtil.getRabbitMqUser());
        factory.setPassword(RabbitMqUtil.getRabbitMqPassword());

        Connection connection = null;

        try {

            if ("true".equalsIgnoreCase(System.getenv("RABBITMQ_SSL"))) {
                factory.useSslProtocol();
            }

            connection = factory.newConnection();

            if (connection.isOpen()) {
                modelAndView.setViewName("rabbitmq");
            } else {
                modelAndView.setViewName("rabbitmq-error");
            }

        } catch (Exception e) {

            LOGGER.error("Failed to connect to RabbitMQ", e);

            modelAndView.setViewName("rabbitmq-error");

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    LOGGER.warn("Failed to close RabbitMQ connection", e);
                }
            }
        }

        return modelAndView;
    }
}