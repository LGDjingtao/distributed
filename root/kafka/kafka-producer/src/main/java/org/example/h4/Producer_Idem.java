package org.example.h4;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * 生产者
 * 配合 acks等级-1来 自动重试 外加幂等性
 * 幂等性 由于生产者在途请求缓冲来保证的
 * 幂等原理是 生产者id + 数据编号来保证的 所以跨会话和跨分区是不能保证幂等性的
 */
@Slf4j
public class Producer_Idem {
    public static void main(String[] args) {
        //创建配置对象
        Properties properties = new Properties();

        //给配置对象加参数
        //1.添加连接
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.28:9092");
        //2.序列化器 KafkaProducer<String, String> 和泛型里面的key value是对应的
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //3.非必要参数
        //acksc参数
        properties.put(ProducerConfig.ACKS_CONFIG, "-1");
        //开启幂等性
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        //重试次数 -开启幂等一定开启重试
        properties.put(ProducerConfig.RETRIES_CONFIG, 5);
        //最大消息阻塞时间
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);


        //创建kafka生产者对象
        //泛型值的是最终发送消息的key 和 value 的类型
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        //发送消息
        for (int i = 1; i <= 20; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("self", "key", "zjtstc" + i);
            producer.send(record);
        }

        producer.close();
    }
}
