package com.kafka.producer.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.model.Movie;
import com.kafka.producer.service.KafkaMessageProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping(value = "/kafka")
public class KafkaMessageProducerController {

    @Autowired
    private KafkaMessageProducer producer;

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestBody Movie movie) throws JsonProcessingException, ExecutionException, InterruptedException {
        this.producer.send(movie);
    }
}
