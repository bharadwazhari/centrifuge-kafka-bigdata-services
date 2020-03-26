package com.test;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class WordCountProcessor implements Processor<String, String> {
    KeyValueStore<String, Long> store;
    ProcessorContext context;
    @Override
    public void init(ProcessorContext processorContext) {
        context = processorContext;
        //processorContext.schedule();
        store = (KeyValueStore<String, Long>) processorContext.getStateStore("Counts");
    }

    @Override
    public void process(String key, String value) {
        String [] wordsArray = value.split(" ");
        for(String word : wordsArray){
            Long count = store.get(word);
            if(count == null){
                store.put(word, 1L);
                context.forward(word, "1");
            } else {
                store.put(word, count + 1);
                context.forward(word, count.toString());
            }
        }
        context.commit();
    }

    public void punctuate(long l) {
        /*KeyValueIterator<String, Long> iter = store.all();
        while (iter.hasNext()){
            KeyValue<String, Long> kv = iter.next();
            System.out.println("KEY: "  + kv.key + " VALUE: " + kv.value);
        }*/
    }

    @Override
    public void close() {

    }
}
