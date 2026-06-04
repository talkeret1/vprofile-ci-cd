package com.visualpathit.account.serviceTest;

import com.visualpathit.account.service.ConsumerServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConsumerServiceImplTest {

    @Test
    public void shouldConsumeMessageWithoutException() {

        ConsumerServiceImpl service = new ConsumerServiceImpl();

        byte[] data = "hello rabbit".getBytes();

        service.consumerMessage(data);

        assertTrue(true);
    }

    @Test
    public void shouldHandleEmptyMessage() {

        ConsumerServiceImpl service = new ConsumerServiceImpl();

        byte[] data = new byte[0];

        service.consumerMessage(data);

        assertTrue(true);
    }

    @Test
    public void shouldHandleNullBytesSafely() {

        ConsumerServiceImpl service = new ConsumerServiceImpl();

        try {
            service.consumerMessage(null);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}