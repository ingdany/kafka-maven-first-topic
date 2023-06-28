package com.kaligent;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

public class Producer {
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        String bootstrapServers = "localhost:9093; localhost:9094; localhost:9095";
        // String groupId = "my-application";
        String topic = "seventh_topic";

        Properties props = new Properties();

        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        // props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class);

      
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, message);

        for (int i = 0; i < 100; i++) {
            String key = "key" + i;
            String value = "value" + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e){
                    if (e == null) {
                        log.info(recordMetadata.topic() + ":" + recordMetadata.partition() + ":" + recordMetadata.offset());
                    }
                }
            });    
        }

        producer.flush();
        producer.close();
       
    }

}