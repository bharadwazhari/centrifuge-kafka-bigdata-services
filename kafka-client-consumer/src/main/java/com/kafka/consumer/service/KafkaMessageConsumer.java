package com.kafka.consumer.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaMessageConsumer {

    @KafkaListener(topics = "my_topic", groupId = "group_id")
    public void consume(String message){
        log.info("Consumed Message -> "+ message);
    }

}
