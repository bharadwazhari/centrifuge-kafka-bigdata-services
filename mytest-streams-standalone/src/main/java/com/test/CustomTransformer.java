package com.test;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

public class CustomTransformer implements Transformer<String, String, KeyValue<String, Long>> {

    private Long cap;

    private String stateStoreName;

    private ProcessorContext context;

    private KeyValueStore<String, Long> kvStore;

    public CustomTransformer(String stateStoreName) {
        this.stateStoreName = stateStoreName;
        this.cap = 1L;
    }

    public CustomTransformer(String stateStoreName, Long cap) {
        this.stateStoreName = stateStoreName;
        this.cap = cap;
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.kvStore = (KeyValueStore<String, Long>) context.getStateStore(stateStoreName);
    }

    @Override
    public KeyValue<String, Long> transform(String key, String value) {
        String [] wordsArray = value.split(" ");
        for(String word : wordsArray){
            Long count = kvStore.get(word);
            if(count == null){
                kvStore.put(word, 1L);
                context.forward(word, "1");
            } else {
                kvStore.put(word, count + 1);
                context.forward(word, count.toString());
            }
        }
        context.commit();
        return null;
    }

    @Override
    public void close() {

    }

    private void findAndFlushCandidates() {
        KeyValueIterator<String, Long> it = kvStore.all();
        while(it.hasNext()) {
            KeyValue<String, Long> entry = it.next();
            if (entry.value >= cap) {
                this.context.forward(entry.key, entry.value);
                kvStore.delete(entry.key);
            }
        }
        it.close();
    }
}
