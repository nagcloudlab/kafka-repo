package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class WordCountDemo {
    public static void main(String[] args) {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KStreamBuilder builder = new KStreamBuilder();
        // 1 - stream from Kafka
        KStream<String, String> textLines = builder.stream("streams-plaintext-input");


        KTable<String,Long> wordCounts=textLines
                // 2 - map values to lowercase
                .mapValues(textLine->textLine.toLowerCase())
                // 3 - flatmap values split by space
                .flatMapValues(textLine -> Arrays.asList(textLine.split("\\W+")))
                // 4 - select key to apply a key (we discard the old key)
                .selectKey((key,word)->word)
                // 5 - group by key before aggregation
                .groupByKey()
                // 6 - count occurences
                .count("Counts");

        // 7 - to in order to write the results back to kafka
        wordCounts.to(Serdes.String(), Serdes.Long(), "streams-wordcount-output");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

    }
}
