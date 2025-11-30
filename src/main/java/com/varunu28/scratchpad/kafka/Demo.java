package com.varunu28.scratchpad.kafka;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Demo {

    static void main() {
        var producerThread = new Thread(new ProducerTask());
        var consumerThread = new Thread(new ConsumerTask());

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class ProducerTask implements Runnable {

        @Override
        public void run() {
            Map<String, Object> producerConfig = Map.of(
                "bootstrap.servers", "localhost:9092",
                "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer", "org.apache.kafka.common.serialization.StringSerializer"
            );
            try (Producer<String, String> producer = new KafkaProducer<>(producerConfig)) {
                var order = """
                    {
                        "orderId": "%s",
                        "customerId": "%s"
                    }
                    """.formatted(UUID.randomUUID().toString(), UUID.randomUUID().toString());
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    "orders", UUID.randomUUID().toString(), order
                );
                Callback callback = (recordMetadata, e) -> {
                    if (e != null) {
                        IO.println("Error while producing message to topic : " + e.getMessage());
                    } else {
                        IO.println("Produced message to topic " + recordMetadata.topic() +
                            " partition " + recordMetadata.partition() +
                            " offset " + recordMetadata.offset());
                    }
                };
                producer.send(record, callback);
                producer.flush();
            }
        }
    }

    static class ConsumerTask implements Runnable {

        @Override
        public void run() {
            Map<String, Object> consumerConfig = Map.of(
                "bootstrap.servers", "localhost:9092",
                // Unique id for each consumer group
                "group.id", "order-processors",
                // default value of offset if no offset is present for the consumer group
                "auto.offset.reset", "earliest",
                "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"
            );

            try (Consumer<String, String> consumer = new KafkaConsumer<>(consumerConfig)) {
                consumer.subscribe(Collections.singletonList("orders"));
                IO.println("Waiting for messages...");
                //noinspection InfiniteLoopStatement
                while (true) {
                    var records = consumer.poll(java.time.Duration.ofMillis(100));
                    for (var record : records) {
                        IO.println("Received message: " + record.value() +
                            " from topic: " + record.topic() +
                            " partition: " + record.partition() +
                            " offset: " + record.offset());
                    }
                }
            }
        }
    }
}
