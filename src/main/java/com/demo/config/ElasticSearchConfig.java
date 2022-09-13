package com.demo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * ElasticSearchConfig
 *
 * @author Ji MingHao
 * @since 2022-09-13 14:22
 */
@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    /**
     * 注入IOC容器
     *
     * @return ElasticsearchClient
     */
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        try (RestClients.ElasticsearchRestClient elasticsearchRestClient = RestClients.create(clientConfiguration)) {
            return elasticsearchRestClient.rest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
