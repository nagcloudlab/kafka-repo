package com.example.bindings;



import com.example.kafka.HadoopRecord;
import com.example.kafka.Notification;
import com.example.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface PosListenerBinding {

    @Input("pos-input-channel")
    KStream<String, PosInvoice> notificationInputStream();


}
