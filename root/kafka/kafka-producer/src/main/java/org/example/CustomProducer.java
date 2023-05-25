package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CustomProducer {
    public static void main(String[] args) {
        //创建配置对象
        Properties properties = new Properties();

        //给配置对象加参数
        //1.添加连接
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.80.128:9092");
        //2.序列化器 KafkaProducer<String, String> 和泛型里面的key value是对应的
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //3.非必要参数
        //批次大小 默认16k
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        //等待时间 默认0ms
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1);
        //缓冲区大小 默认32M
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);

        //创建kafka生产者对象
        //泛型值的是最终发送消息的key 和 value 的类型
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        //发送消息
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>("firs","zjtstc"+i));
        }

        producer.close();
    }
}
