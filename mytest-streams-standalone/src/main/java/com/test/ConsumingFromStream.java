package com.test;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class ConsumingFromStream {

    public static void main(String... args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "PrintRequestConsumerApplication");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.t);
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> printRequests = builder.stream("PrintRequest");
        printRequests.foreach((key,value) -> {
            if(key.startsWith("1")) try {
                throw new Exception("Invalid Key - Checked");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(key.startsWith("0")) throw new RuntimeException("Invalid Key - Runtime");
            System.out.println("KEY => "+key+"  => "+ value);
        });
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.setUncaughtExceptionHandler((Thread thread, Throwable e) -> {
            System.out.println("Exception For => "+ thread.getState() + e);
        });
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }


}
