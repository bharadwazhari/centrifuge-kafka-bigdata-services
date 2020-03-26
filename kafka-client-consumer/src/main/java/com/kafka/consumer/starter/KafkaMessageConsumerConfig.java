package com.kafka.consumer.starter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@EqualsAndHashCode(callSuper=false)
@Configuration
@ComponentScan(basePackages = "com.kafka.consumer")
public class KafkaMessageConsumerConfig {

}
