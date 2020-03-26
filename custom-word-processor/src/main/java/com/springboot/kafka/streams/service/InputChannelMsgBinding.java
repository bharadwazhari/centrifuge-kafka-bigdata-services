package com.springboot.kafka.streams.service;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface InputChannelMsgBinding {
    final String REQUESTS_IN = "requests-in";
    @Input(InputChannelMsgBinding.REQUESTS_IN)
    KStream<String, String> requestsIn();
}
