package com.elasticsearch.starter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

@Data
@EqualsAndHashCode(callSuper=false)
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.elasticsearch.repository")
public class ElasticSearchConfig extends ElasticsearchConfigurationSupport {
    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.clustername}")
    private String esClusterName;

    @Bean
    public Client client() throws Exception {

        Settings esSettings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("cluster.name", esClusterName)
                .build();
        TransportClient client = new PreBuiltTransportClient(esSettings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(esHost), esPort));
        return client;
    }

    /*@Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }*/

    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchTemplate elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client(), entityMapper());
    }

}
