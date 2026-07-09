package com.visualpathit.account.utils;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import com.visualpathit.account.beans.Components;

@Component
public class ElasticsearchClientFactory {

    private final Components components;

    public ElasticsearchClientFactory(Components components) {
        this.components = components;
    }

    public RestHighLevelClient createClient() {

        String host = components.getElasticsearchHost();
        String port = components.getElasticsearchPort();
        String scheme = components.getElasticsearchScheme();

        if (host == null || host.isBlank()
                || port == null || port.isBlank()
                || scheme == null || scheme.isBlank()) {
            throw new IllegalStateException("Elasticsearch configuration is missing");
        }

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, Integer.parseInt(port), scheme)));
    }
}