package com.wkb.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author WuKB
 * @Date 2020/5/19
 */
public interface IKafkaService {
    /**
     * <h2>消费优惠券 Kafka 消息</h2>
     * @param record {@link ConsumerRecord}
     * */
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);
}
