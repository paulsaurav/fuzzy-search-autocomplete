package com.search.medicinesearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.elasticsearch.core.InfoResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PreDestroy;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    private RestClient restClient;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        restClient = RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        ElasticsearchClient client = new ElasticsearchClient(transport);


        try {
            InfoResponse info = client.info();
            System.out.println("Connected to Elasticsearch cluster: " + info.clusterName());
        } catch (Exception e) {
            System.err.println("Unable to connect to Elasticsearch: " + e.getMessage());
            throw new RuntimeException("Elasticsearch is not reachable. Shutting down.");
        }

        return client;
    }

    @PreDestroy
    public void close() throws Exception {
        if (restClient != null) {
            restClient.close();
        }
    }
}
