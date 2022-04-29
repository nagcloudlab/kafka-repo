package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

public class StreamingTableApp {
    public static void main(String[] args) {

        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, String> source = streamsBuilder.stream("streams-plaintext-input");

        source
                .flatMapValues(textLine-> Arrays.asList(textLine.toLowerCase(Locale.getDefault()).split("\\W+")))
                .groupBy((key,word)->word)
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
                .toStream()
                .peek((k,v)->{
                    System.out.println(k);
                    System.out.println(v);
                })
                .to("streams-wordcount-output1", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
        }));

    }
}
