package com.visualpathit.account.serviceTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.visualpathit.account.service.ProducerServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProducerServiceImplTest {

    @Test
    public void shouldReturnResponse() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel()).thenReturn(channel);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("hello rabbit");

        assertEquals("response", result);

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

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel()).thenReturn(channel);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        service.produceMessage("");

        verify(channel).basicPublish(eq("messages"), eq(""), isNull(), any(byte[].class));
    }

    @Test
    public void shouldHandleNullMessageSafely() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel()).thenReturn(channel);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        service.produceMessage(null);

        verify(channel).basicPublish(eq("messages"), eq(""), isNull(), any(byte[].class));
    }

    @Test
    public void shouldHandleNullConnectionGracefully() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);

        doReturn(null)
                .when(factory)
                .newConnection();

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("test");

        assertEquals("response", result);
    }

    @Test
    public void shouldHandleChannelNullGracefully() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel()).thenReturn(null);

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("test");

        assertEquals("response", result);
    }

    @Test
    public void shouldHandleIOException() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel())
                .thenThrow(new RuntimeException("IO_ERROR"));

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("test");

        assertEquals("response", result);
    }

    @Test
    public void shouldHandleTimeoutException() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel())
                .thenThrow(new RuntimeException("TIMEOUT_ERROR"));

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("test");

        assertEquals("response", result);
    }

    @Test
    public void shouldHandleGenericException() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel())
                .thenThrow(new RuntimeException("BOOM"));

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("test");

        assertEquals("response", result);
    }

    @Test
    public void shouldHandleCloseExceptionGracefully() throws Exception {

        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);

        doReturn(connection)
                .when(factory)
                .newConnection();

        when(connection.createChannel()).thenReturn(channel);

        doThrow(new RuntimeException("close fail"))
                .when(channel).close();

        doThrow(new RuntimeException("close fail"))
                .when(connection).close();

        ProducerServiceImpl service = new ProducerServiceImpl(factory);

        String result = service.produceMessage("test");

        assertEquals("response", result);
    }
}