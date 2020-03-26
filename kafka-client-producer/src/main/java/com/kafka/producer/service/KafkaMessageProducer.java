package com.kafka.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class KafkaMessageProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topicname}")
    private String topic;

    public void send(Object message) throws JsonProcessingException, ExecutionException, InterruptedException {
        log.debug("************* Start of Message ********************");
        final ProducerRecord record = new ProducerRecord<>(topic, 0, KeyUtils.keyIncrement, new ObjectMapper().writeValueAsString(message));
        log.debug(message);
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(record);
        //kafkaTemplate.send(topic, null, new ObjectMapper().writeValueAsString(message));
        SendResult<Integer, String> result  = future.get();
        RecordMetadata rm = result.getRecordMetadata();
        log.debug("************* End of Message ********************");
    }
}
