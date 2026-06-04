package com.visualpathit.account.serviceTest;

import com.visualpathit.account.service.ProducerServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProducerServiceImplTest {

    @Test
    public void shouldReturnResponse() {

        ProducerServiceImpl service = new ProducerServiceImpl();

        String result = service.produceMessage("hello");

        assertEquals("response", result);
    }

    @Test
    public void shouldReturnResponseEvenWithEmptyMessage() {

        ProducerServiceImpl service = new ProducerServiceImpl();

        String result = service.produceMessage("");

        assertEquals("response", result);
    }

    @Test
    public void shouldReturnResponseEvenWithNullMessage() {

        ProducerServiceImpl service = new ProducerServiceImpl();

        String result = service.produceMessage(null);

        assertEquals("response", result);
    }
}