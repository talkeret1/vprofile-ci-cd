package com.visualpathit.account.serviceTest;

import org.junit.Test;

import com.visualpathit.account.service.ConsumerServiceImpl;

public class ConsumerServiceImplTest {

    @Test
    public void shouldConsumeMessage() {

        ConsumerServiceImpl service = new ConsumerServiceImpl();

        service.consumerMessage("hello".getBytes());
    }
}