package org.example.h1;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 消费者
 * 最基础消费者实现
 */
@Slf4j
public class Customer {
    public static void main(String[] args) {
        //创建配置对象
        Properties properties = new Properties();

        //给配置对象加参数
        //1.添加连接
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.28:9092");
        //2.序列化器 KafkaProducer<String, String> 和泛型里面的key value是对应的
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "Comag");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList("self"));

        try {
            while (true) {
                ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : poll) {
                    log.info("接受: {} :from:self", record);
                }
            }
        } catch (Exception e) {
            log.error("消费者出错", e);
        } finally {
            consumer.close();
        }


        consumer.close();
    }
}
