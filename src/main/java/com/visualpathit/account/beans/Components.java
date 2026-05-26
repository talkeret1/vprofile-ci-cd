package com.visualpathit.account.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Components {

	// ======================
	// Memcached
	// ======================

	@Value("${memcached.active.host}")
	private String activeHost;

	@Value("${memcached.active.port}")
	private String activePort;

	@Value("${memcached.standBy.host}")
	private String standByHost;

	@Value("${memcached.standBy.port}")
	private String standByPort;

	// ======================
	// RabbitMQ
	// ======================

	@Value("${rabbitmq.address}")
	private String rabbitMqHost;

	@Value("${rabbitmq.port}")
	private String rabbitMqPort;

	@Value("${rabbitmq.username}")
	private String rabbitMqUser;

	@Value("${rabbitmq.password}")
	private String rabbitMqPassword;

	// ======================
	// Elasticsearch / OpenSearch
	// ======================

	@Value("${elasticsearch.host}")
	private String elasticsearchHost;

	@Value("${elasticsearch.port}")
	private String elasticsearchPort;

	@Value("${elasticsearch.cluster}")
	private String elasticsearchCluster;

	@Value("${elasticsearch.node}")
	private String elasticsearchNode;

	@Value("${elasticsearch.scheme}")
	private String elasticsearchScheme;

	// ======================
	// Getters only (no setters)
	// ======================

	public String getActiveHost() {
		return activeHost;
	}

	public String getActivePort() {
		return activePort;
	}

	public String getStandByHost() {
		return standByHost;
	}

	public String getStandByPort() {
		return standByPort;
	}

	public String getRabbitMqHost() {
		return rabbitMqHost;
	}

	public String getRabbitMqPort() {
		return rabbitMqPort;
	}

	public String getRabbitMqUser() {
		return rabbitMqUser;
	}

	public String getRabbitMqPassword() {
		return rabbitMqPassword;
	}

	public String getElasticsearchHost() {
		return elasticsearchHost;
	}

	public String getElasticsearchPort() {
		return elasticsearchPort;
	}

	public String getElasticsearchCluster() {
		return elasticsearchCluster;
	}

	public String getElasticsearchNode() {
		return elasticsearchNode;
	}

	public String getElasticsearchScheme() {
		return elasticsearchScheme;
	}
}