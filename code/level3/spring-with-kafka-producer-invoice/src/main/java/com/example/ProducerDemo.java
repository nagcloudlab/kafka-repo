package com.example;

import com.example.services.KafkaProducerService;
import com.example.services.datagenerator.InvoiceGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class ProducerDemo implements ApplicationRunner {

    private final KafkaProducerService producerService;
    private final InvoiceGenerator invoiceGenerator;

    @Value("${application.configs.invoice.count}")
    private int INVOICE_COUNT;


    public ProducerDemo(KafkaProducerService producerService, InvoiceGenerator invoiceGenerator) {
        this.producerService = producerService;
        this.invoiceGenerator = invoiceGenerator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerDemo.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < INVOICE_COUNT; i++) {
            producerService.sendMessage(invoiceGenerator.getNextInvoice());
            Thread.sleep(1000);
        }
    }
}
