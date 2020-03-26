package com.test;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WordCountApp {

    public static void main(String[] args) {
        Properties props = prepareProperties();
        Topology topology = createTopology();
        final KafkaStreams streams = new KafkaStreams(topology, props);
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

    private static Topology createTopology() {
        Topology builder = new Topology();
        builder.addSource("sourcetopic", "streams-plaintext-input")
                .addProcessor("processstep", WordCountProcessor::new, "sourcetopic")
                .addStateStore(prepareStateStore(builder), "processstep")
                .addSink("sinktopic", "streams-wordcount-output", "processstep");
        System.out.println(builder.describe());
        return builder;
    }

    private static StoreBuilder<KeyValueStore<String, Long>> prepareStateStore(Topology topology) {
        Map<String, String> changelogConfig = new HashMap();
        //changelogConfig.put("min.insyc.replicas", "1");

        StoreBuilder<KeyValueStore<String, Long>> wordCountsStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("Counts"),
                Serdes.String(),
                Serdes.Long())
                .withLoggingEnabled(changelogConfig);
        return wordCountsStore;
    }
}
