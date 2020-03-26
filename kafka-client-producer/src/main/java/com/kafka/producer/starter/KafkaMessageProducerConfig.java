package com.kafka.producer.starter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Data
@EqualsAndHashCode(callSuper=false)
@Configuration
@ComponentScan(basePackages = "com.kafka.producer")
public class KafkaMessageProducerConfig {

}
