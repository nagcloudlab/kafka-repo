package com.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAdminConfiguration {


    @Bean
    public NewTopic topic1(){
        return
                TopicBuilder.name("hobbit")
                        .build();
    }


}
