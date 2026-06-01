package com.visualpathit.account.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visualpathit.account.beans.Components;

@Service
public class ElasticsearchUtil {

    private static Components object;

    @Autowired
    public void setComponents(Components object) {
        ElasticsearchUtil.object = object;
    }

    public static RestHighLevelClient getRestHighLevelClient() {

        System.out.println("Creating Elasticsearch client...");

        if (object == null) {
            throw new RuntimeException("Components not initialized (Spring injection failed)");
        }

        String host = object.getElasticsearchHost();
        String port = object.getElasticsearchPort();
        String scheme = object.getElasticsearchScheme();

        System.out.println("=== DEBUG ELASTICSEARCH ===");
        System.out.println("HOST = " + host);
        System.out.println("PORT = " + port);
        System.out.println("SCHEME = " + scheme);
        System.out.println("===========================");

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, Integer.parseInt(port), scheme)));
    }
}