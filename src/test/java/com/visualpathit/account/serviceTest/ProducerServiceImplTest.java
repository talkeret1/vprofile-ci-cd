package com.visualpathit.account.serviceTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.visualpathit.account.service.ProducerServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

public class ProducerServiceImplTest {

    @Test
    public void shouldReturnResponse() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);

        when(factory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("hello rabbit");

        assertEquals("response", result);

        verify(factory).newConnection();
        verify(connection).createChannel();
        verify(channel).exchangeDeclare("messages", "fanout");
        verify(channel).basicPublish(eq("messages"), eq(""), isNull(), any(byte[].class));
        verify(channel).close();
        verify(connection).close();
    }

    @Test
    public void shouldHandleEmptyMessage() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);

        when(factory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("");

        assertEquals("response", result);
    }

    @Test
    public void shouldHandleNullMessageSafely() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);

        when(factory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage(null);

        assertEquals("response", result);
    }

    @Test
    public void shouldHandleNullConnectionGracefully() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        when(factory.newConnection()).thenReturn(null);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("test");

        assertEquals("response", result);
    }
}