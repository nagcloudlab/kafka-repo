package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;

public class FavouriteColourDemo {
    public static void main(String[] args) {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "favourite-colour-java");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // we disable the cache to demonstrate all the "steps" involved in the transformation - not recommended in prod
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

        final KStreamBuilder builder = new KStreamBuilder();

        // Step 1: We create the topic of users keys to colours
        KStream<String, String> textLines = builder.stream("favourite-colour-input");

        KStream<String,String> usersAndColours=textLines
                .filter(((key, value) -> value.contains(","))) // null-Nag,red => true
                .selectKey((key,value)->value.split(",")[0].toLowerCase()) // nag-Nag,red
                .mapValues((value)->value.split(",")[1].toLowerCase()) // nag-red
                .filter((user,colour)-> Arrays.asList("red","green","blue").contains(colour)); // true

        usersAndColours.to("user-keys-and-colours");

        // step 2 - we read that topic as a KTable so that updates are read correctly
        KTable<String, String> usersAndColoursTable = builder.table("user-keys-and-colours");

        // step 3 - we count the occurences of colours
        KTable<String,Long> favouriteColours=usersAndColoursTable
                // 5 - we group by colour within the KTable
                .groupBy((user,colour)->new KeyValue<>(colour,colour))
                .count();

        // 6 - we output the results to a Kafka Topic - don't forget the serializers
        favouriteColours.to(Serdes.String(), Serdes.Long(),"favourite-colour-output");

        KafkaStreams streams = new KafkaStreams(builder, config);
        // only do this in dev - not in prod
        streams.cleanUp();
        streams.start();

        // print the topology
        System.out.println(builder);

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }
}
