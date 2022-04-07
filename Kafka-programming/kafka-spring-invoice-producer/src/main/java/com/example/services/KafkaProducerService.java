package com.example.services;

import com.example.model.PosInvoice;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaProducerService {

    @Value("${application.configs.topic.name}")
    private String TOPIC_NAME;

    private final KafkaTemplate<String, PosInvoice> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, PosInvoice> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(PosInvoice invoice) {
        log.info(String.format("Producing Invoice No: %s Customer Type: %s",
                invoice.getInvoiceNumber(),
                invoice.getCustomerType()));
        kafkaTemplate.send(TOPIC_NAME, invoice.getStoreID(), invoice);
    }

}
