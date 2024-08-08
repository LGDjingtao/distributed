package org.example.h2;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


/**
 * 生产者者
 * 拦截器实现
 */
@Slf4j
public class Producer_Interceptor {
    public static void main(String[] args) {
        //创建配置对象
        Properties properties = new Properties();

        //给配置对象加参数
        //1.添加连接
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.1.28:9092");
        //2.序列化器 KafkaProducer<String, String> 和泛型里面的key value是对应的
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //3.非必要参数
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ValueInterceptor.class.getName());
        //创建kafka生产者对象
        //泛型值的是最终发送消息的key 和 value 的类型
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        //发送消息
        for (int i = 0; i < 2; i++) {
            producer.send(new ProducerRecord<String, String>("self","key","zjtstc"+i));
            log.info("发送:"+"zjtstc"+i + ":to:self" );
        }
        producer.close();
    }
}
