package org.example.Producer;

import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class ProducerApp {
    public static void main(String[] args) throws InterruptedException {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        System.out.println("Sending messages... 🚀");

        producer.send(new ProducerRecord<>("test-topic", "Hello Kafka 1"));
        producer.send(new ProducerRecord<>("test-topic", "Hello Kafka 2"));


        Thread.sleep(2000);
        producer.flush();
        producer.close();

        System.out.println("Messages sent ✅");
    }
}