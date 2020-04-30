package com.test;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WordCountApp {

    public static void main(String[] args) {
        Properties props = prepareProperties();
        //Topology topology = createTopology();
        Topology topology =  combineDslNProcessApiTopology();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        streams.setUncaughtExceptionHandler((Thread thread, Throwable e) -> {
            System.out.println("Exception For => "+ thread.getState() + e);
        });
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }

    private static Properties prepareProperties() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        //streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        return streamsConfiguration;
    }

    /*
     *  bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic streams-wordcount-output --property print.key=true --property key.separator="="
     */
    private static Topology createTopology() {
        Topology builder = new Topology();
        builder.addSource("sourcetopic", "streams-plaintext-input")
                .addProcessor("processstep", WordCountProcessor::new, "sourcetopic")
                .addStateStore(prepareStateStore(), "processstep")
                .addSink("sinktopic", "streams-wordcount-output", "processstep");
        System.out.println(builder.describe());
        return builder;
    }

    private static Topology combineDslNProcessApiTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        prepareStateStore(builder);
        KStream<String, String> sourceTopicStream = builder.stream("streams-plaintext-input");
        sourceTopicStream.transform(() -> new CustomTransformer("COUNTS"),  "COUNTS")
        .to("streams-wordcount-output");
        Topology topology = builder.build();
        System.out.println(topology.describe());
        return topology;
    }

    private static StoreBuilder<KeyValueStore<String, Long>> prepareStateStore() {
        Map<String, String> changelogConfig = new HashMap();
        //changelogConfig.put("min.insyc.replicas", "1");

        StoreBuilder<KeyValueStore<String, Long>> wordCountsStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("COUNTS"),
                Serdes.String(),
                Serdes.Long())
                .withLoggingEnabled(changelogConfig);
        return wordCountsStore;
    }

    private static StoreBuilder<KeyValueStore<String, Long>> prepareStateStore(StreamsBuilder builder) {
        Map<String, String> changelogConfig = new HashMap();
        //changelogConfig.put("min.insyc.replicas", "1");

        StoreBuilder<KeyValueStore<String, Long>> wordCountsStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("COUNTS"),
                Serdes.String(),
                Serdes.Long());
                //.withLoggingEnabled(changelogConfig);

        builder.addStateStore(wordCountsStore);
        return wordCountsStore;
    }
}
