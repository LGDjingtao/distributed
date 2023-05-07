package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class CustomProducer {
    public static void main(String[] args) {
        //创建配置对象
        Properties properties = new Properties();

        //给配置对象加参数
        //1.添加连接
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.80.128:");

        //创建kafka生产者对象
        //泛型值的是最终发送消息的key 和 value 的类型
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

    }
}
