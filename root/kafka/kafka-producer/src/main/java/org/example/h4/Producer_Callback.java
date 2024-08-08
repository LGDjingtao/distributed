package org.example.h4;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 生产者者
 * 最基础消费者实现
 */
@Slf4j
public class Producer_Callback {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建配置对象
        Properties properties = new Properties();

        //给配置对象加参数
        //1.添加连接
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.1.28:9092");
        //2.序列化器 KafkaProducer<String, String> 和泛型里面的key value是对应的
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //3.非必要参数

        //创建kafka生产者对象
        //泛型值的是最终发送消息的key 和 value 的类型
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        //发送消息
        for (int i = 1; i < 2; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("self", "key", "zjtstc" + i);
            Future<RecordMetadata> send = producer.send(record, (r, e) -> {
                log.info("数据发送成功" + r);
            });

            log.info("数据发送");
            //这里会阻塞，数据同步发送
            send.get();
        }

        producer.close();
    }
}
