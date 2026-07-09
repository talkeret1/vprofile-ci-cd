package com.visualpathit.account.utilsTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.utils.ElasticsearchClientFactory;
import org.junit.jupiter.api.Test;
import org.opensearch.client.RestHighLevelClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElasticsearchClientFactoryTest {

    @Test
    void createClientShouldReturnClientWhenConfigurationIsValid() {

        Components components = mock(Components.class);

        when(components.getElasticsearchHost()).thenReturn("localhost");
        when(components.getElasticsearchPort()).thenReturn("9200");
        when(components.getElasticsearchScheme()).thenReturn("http");

        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(components);

        RestHighLevelClient client = factory.createClient();

        assertNotNull(client);

        try {
            client.close();
        } catch (Exception ignored) {
        }
    }

    @Test
    void createClientShouldThrowExceptionWhenHostIsNull() {

        Components components = mock(Components.class);

        when(components.getElasticsearchHost()).thenReturn(null);
        when(components.getElasticsearchPort()).thenReturn("9200");
        when(components.getElasticsearchScheme()).thenReturn("http");

        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(components);

        assertThrows(
                IllegalStateException.class,
                factory::createClient);
    }

    @Test
    void createClientShouldThrowExceptionWhenPortIsBlank() {

        Components components = mock(Components.class);

        when(components.getElasticsearchHost()).thenReturn("localhost");
        when(components.getElasticsearchPort()).thenReturn(" ");
        when(components.getElasticsearchScheme()).thenReturn("http");

        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(components);

        assertThrows(
                IllegalStateException.class,
                factory::createClient);
    }

    @Test
    void createClientShouldThrowExceptionWhenSchemeIsBlank() {

        Components components = mock(Components.class);

        when(components.getElasticsearchHost()).thenReturn("localhost");
        when(components.getElasticsearchPort()).thenReturn("9200");
        when(components.getElasticsearchScheme()).thenReturn("");

        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(components);

        assertThrows(
                IllegalStateException.class,
                factory::createClient);
    }

    @Test
    void shouldThrowExceptionWhenConfigMissing() {

        Components components = mock(Components.class);

        when(components.getElasticsearchHost()).thenReturn("");
        when(components.getElasticsearchPort()).thenReturn("9200");
        when(components.getElasticsearchScheme()).thenReturn("http");

        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(components);

        assertThrows(
                IllegalStateException.class,
                factory::createClient);
    }

    @Test
    void createClientShouldNotThrowExceptionWhenConfigIsValid() {

        Components components = mock(Components.class);

        when(components.getElasticsearchHost()).thenReturn("localhost");
        when(components.getElasticsearchPort()).thenReturn("9200");
        when(components.getElasticsearchScheme()).thenReturn("http");

        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(components);

        try {
            RestHighLevelClient client = factory.createClient();
            assertNotNull(client);
            client.close();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}