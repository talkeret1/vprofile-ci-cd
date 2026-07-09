package com.visualpathit.account.serviceTest;

import com.visualpathit.account.service.ConsumerServiceImpl;
import org.junit.Test;

public class ConsumerServiceImplTest {

    @Test
    public void shouldConsumeMessage() {
        ConsumerServiceImpl service = new ConsumerServiceImpl();

        service.consumerMessage("hello rabbit".getBytes());
    }

    @Test
    public void shouldHandleEmptyMessage() {
        ConsumerServiceImpl service = new ConsumerServiceImpl();

        service.consumerMessage(new byte[0]);
    }

    @Test
    public void shouldHandleNullSafely() {
        ConsumerServiceImpl service = new ConsumerServiceImpl();

        service.consumerMessage(null);
    }
}