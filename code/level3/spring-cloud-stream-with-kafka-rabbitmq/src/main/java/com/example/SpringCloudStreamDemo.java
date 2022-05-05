package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class SpringCloudStreamDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamDemo.class, args);
    }


    //suffix-in-0
    //suffix-out-0
    @Bean
    public Function<String,String> suffix(){
        return inputMessage->{
            return inputMessage+"-ibm";
        };
    }

    //upperCase-in-0
    //upperCase-out-0
    @Bean
    public Function<String,String> upperCase(){
        return inputMessage->{
            return inputMessage.toUpperCase();
        };
    }

    //consume-in-0
    @Bean
    public Consumer<String> consume(){
        return (inputMessage)->{
            System.out.println(inputMessage);
        };
    }

}
