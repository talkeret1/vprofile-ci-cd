package com.visualpathit.account.beansTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.visualpathit.account.beans.Components;

public class ComponentsTest {

    @Test
    public void shouldSetAndGetAllProperties() {

        Components c = new Components();

        c.setActiveHost("activeHost");
        c.setActivePort("11211");
        c.setStandByHost("standbyHost");
        c.setStandByPort("11212");

        c.setRabbitMqHost("rabbit");
        c.setRabbitMqPort("5672");
        c.setRabbitMqUser("user");
        c.setRabbitMqPassword("password");

        c.setElasticsearchHost("elastic");
        c.setElasticsearchPort("9200");
        c.setElasticsearchCluster("cluster");
        c.setElasticsearchNode("node");

        assertEquals("activeHost", c.getActiveHost());
        assertEquals("11211", c.getActivePort());
        assertEquals("standbyHost", c.getStandByHost());
        assertEquals("11212", c.getStandByPort());

        assertEquals("rabbit", c.getRabbitMqHost());
        assertEquals("5672", c.getRabbitMqPort());
        assertEquals("user", c.getRabbitMqUser());
        assertEquals("password", c.getRabbitMqPassword());

        assertEquals("elastic", c.getElasticsearchHost());
        assertEquals("9200", c.getElasticsearchPort());
        assertEquals("cluster", c.getElasticsearchCluster());
        assertEquals("node", c.getElasticsearchNode());
    }
}