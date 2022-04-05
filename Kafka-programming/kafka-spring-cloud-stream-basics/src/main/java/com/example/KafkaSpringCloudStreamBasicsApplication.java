package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class KafkaSpringCloudStreamBasicsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringCloudStreamBasicsApplication.class, args);
    }

    // Function
    // Consumer
    // Supplier

    // data-in-0
    // data-out-0
    @Bean
    public Function<String,String> data(){
        return data->{
            return data.concat("-ibm");
        };
    }

    // toUpperCase-in-0
    // toUpperCase-out-0
    @Bean
    public Function<String,String> toUpperCase(){
        return str->{
            return str.toUpperCase();
        };
    }

    // consumer-in-0
    @Bean
    public Consumer<String> consumer(){
        return record->{
            System.out.println("consumed - "+record.toString());
        };
    }


}
