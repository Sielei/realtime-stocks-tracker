package com.hs.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;


public interface KafkaConsumer<V extends SpecificRecordBase> {
    void receive(V message);
}
