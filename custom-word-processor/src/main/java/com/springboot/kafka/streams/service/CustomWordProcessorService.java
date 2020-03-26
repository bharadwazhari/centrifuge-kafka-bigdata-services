package com.springboot.kafka.streams.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

//@Service
@Log4j2
//@Data
public class CustomWordProcessorService {

    @Autowired
    private ApplicationContext applicationContext;
    private String stateStoreName = "counterKeyValueStore";
    private Long cap = 7L;

    public void initializeStateStores() throws Exception {
        StreamsBuilderFactoryBean streamsBuilderFactoryBean =
                applicationContext.getBean("&stream-builder-requestListener", StreamsBuilderFactoryBean.class);
        StreamsBuilder streamsBuilder = streamsBuilderFactoryBean.getObject();
        StoreBuilder<KeyValueStore<String, Long>> keyValueStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(stateStoreName), Serdes.String(), Serdes.Long());
        streamsBuilder.addStateStore(keyValueStoreBuilder);
    }

    @StreamListener(InputChannelMsgBinding.REQUESTS_IN)
    public void requestListener(@Input(InputChannelMsgBinding.REQUESTS_IN) KStream<String, String> requestsIn) {
        try {
            initializeStateStores();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        requestsIn
                .transform(() -> new CustomTransformer(stateStoreName, cap), stateStoreName)
                .foreach((k,v) -> {
                    log.info(k + ":" + v);
                });
    }

}
