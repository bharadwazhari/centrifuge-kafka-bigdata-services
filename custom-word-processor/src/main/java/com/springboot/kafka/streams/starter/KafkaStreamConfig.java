package com.springboot.kafka.streams.starter;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import com.springboot.kafka.streams.service.InputChannelMsgBinding;

/*@Data
@EqualsAndHashCode(callSuper=false)
@ComponentScan(basePackages = "com.springboot.kafka.streams")
@Configuration
@EnableKafkaStreams
@EnableBinding(InputChannelMsgBinding.class)*/
public class KafkaStreamConfig {
}
