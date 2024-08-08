package org.example.h2;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 自定义拦截器
 * 1. 实现ProducerInterceptor接口
 * 2. 定义泛型
 * 3. 重写方法
 */
@Slf4j
public class ValueInterceptor implements ProducerInterceptor<String,String> {

    /**
     * 发送数据时，调用
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
       return new ProducerRecord<String, String>(producerRecord.topic(),producerRecord.key(),producerRecord.value() + "tao");
    }

    /**
     * 发送数据完毕，服务器响应
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        log.info("发送数据完毕");
    }

    /**
     * 生产者对象关闭时调用
     */
    @Override
    public void close() {
        log.info("生产者对象关闭");
    }

    @Override
    /**
     * 创建生产对象时调用
     */
    public void configure(Map<String, ?> map) {
        log.info("创建生产对象");
    }
}
