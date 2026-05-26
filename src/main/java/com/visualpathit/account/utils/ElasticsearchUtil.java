package com.visualpathit.account.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visualpathit.account.beans.Components;

@Service
public class ElasticsearchUtil {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchUtil.class);

    private static Components components;

    @Autowired
    public void setComponents(Components components) {
        ElasticsearchUtil.components = components;
    }

    public static RestHighLevelClient getRestHighLevelClient() {

        logger.info("Creating Elasticsearch client...");

        if (components == null) {
            throw new RuntimeException(
                    "Components not initialized (Spring injection failed)");
        }

        String host = components.getElasticsearchHost();
        String port = components.getElasticsearchPort();
        String scheme = components.getElasticsearchScheme();

        if (host == null || port == null || scheme == null) {
            throw new RuntimeException(
                    "Elasticsearch configuration is missing (host/port/scheme)");
        }

        logger.debug("=== ELASTICSEARCH CONFIG ===");
        logger.debug("HOST = {}", host);
        logger.debug("PORT = {}", port);
        logger.debug("SCHEME = {}", scheme);
        logger.debug("============================");

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host,
                                Integer.parseInt(port),
                                scheme)));
    }
}