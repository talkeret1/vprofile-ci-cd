package com.visualpathit.account.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.visualpathit.account.beans.Components;

@Service
public class ElasticsearchUtil {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchUtil.class);

    private final Components components;

    public ElasticsearchUtil(Components components) {
        this.components = components;
    }

    public RestHighLevelClient getRestHighLevelClient() {

        String host = components.getElasticsearchHost();
        String port = components.getElasticsearchPort();
        String scheme = components.getElasticsearchScheme();

        logger.info("=== ELASTIC DEBUG ===");
        logger.info("HOST = {}", host);
        logger.info("PORT = {}", port);
        logger.info("SCHEME = {}", scheme);

        if (host == null || host.isEmpty()) {
            throw new RuntimeException("ELASTICSEARCH_HOST is missing in ECS env");
        }

        if (port == null || port.isEmpty()) {
            port = "443";
        }

        if (scheme == null || scheme.isEmpty()) {
            scheme = "https";
        }

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, Integer.parseInt(port), scheme)));
    }
}
