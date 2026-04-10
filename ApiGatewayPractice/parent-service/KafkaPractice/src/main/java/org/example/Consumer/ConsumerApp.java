package org.example.Consumer;

import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.*;

public class ConsumerApp {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "group2");
        props.put("auto.offset.reset", "earliest");

        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("test-topic"));

        System.out.println("Consumer started... Waiting for messages 🚀");

        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(500)); // ✅ FIXED

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Received: " + record.value());
            }
        }
    }
}

// docker exec -it kafka /opt/kafka/bin/kafka-consumer-groups.sh \ --bootstrap-server localhost:9092 \ --delete \ --group group1
