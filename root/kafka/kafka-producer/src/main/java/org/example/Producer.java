package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
public class Producer {
    public static void main(String[] args) {
        //创建配置对象
        Properties properties = new Properties();

        //给配置对象加参数
        //1.添加连接
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.1.28:9092");
        //2.序列化器 KafkaProducer<String, String> 和泛型里面的key value是对应的
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());        //3.非必要参数
        //批次大小 默认16k
//        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
//        //等待时间 默认0ms
//        properties.put(ProducerConfig.LINGER_MS_CONFIG,1);
//        //缓冲区大小 默认32M
//        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);

        //创建kafka生产者对象
        //泛型值的是最终发送消息的key 和 value 的类型
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        //发送消息
        for (int i = 20; i < 30; i++) {
            producer.send(new ProducerRecord<String, String>("self","key","zjtstc"+i));
            log.info("发送:"+"zjtstc"+i + ":to:self" );
        }

        producer.close();
    }
}
