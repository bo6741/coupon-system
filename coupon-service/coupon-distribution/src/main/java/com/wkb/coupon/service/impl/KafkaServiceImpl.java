package com.wkb.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.constant.Constant;
import com.wkb.coupon.constant.CouponStatus;
import com.wkb.coupon.dao.CouponDao;
import com.wkb.coupon.entity.Coupon;
import com.wkb.coupon.service.IKafkaService;
import com.wkb.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * kafka服务接口实现 -> 把Redis的状态变化持久化
 * @author WuKB
 * @Date 2020/5/23
 */
@Slf4j
@Service
public class KafkaServiceImpl implements IKafkaService {

    @Autowired
    private CouponDao couponDao;

    /**
     * 消费kafka消息
     * @param record {@link ConsumerRecord}
     */
    @Override
    @KafkaListener(topics = {Constant.TOPIC}, groupId = "coupon-kafka")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()){
            Object msg = kafkaMessage.get();
            log.info("receive message from kafka : {}", msg.toString());
            CouponKafkaMessage couponInfo = JSON.parseObject(msg.toString(), CouponKafkaMessage.class);
            CouponStatus status = CouponStatus.getByCode(couponInfo.getStatus());

            switch (status){
                case USED:
                    processUsedCoupons(couponInfo, status);
                    break;
                case USABLE:
                    break;
                case EXPIRED:
                    processExpiredCoupons(couponInfo, status);
                    break;
            }
        }
    }

    /**
     * <h2>处理已使用的用户优惠券</h2>
     * */
    private void processUsedCoupons(CouponKafkaMessage kafkaMessage,
                                    CouponStatus status) {
        // TODO 给用户发送短信
        processCouponsByStatus(kafkaMessage, status);
    }

    /**
     * <h2>处理过期的用户优惠券</h2>
     * */
    private void processExpiredCoupons(CouponKafkaMessage kafkaMessage,
                                       CouponStatus status) {
        // TODO 给用户发送推送
        processCouponsByStatus(kafkaMessage, status);
    }

    /**
     * <h2>根据状态处理优惠券信息</h2>
     * */
    private void processCouponsByStatus(CouponKafkaMessage kafkaMessage,
                                        CouponStatus status) {
        List<Coupon> coupons = couponDao.findAllById(
                kafkaMessage.getIds()
        );
        if (CollectionUtils.isEmpty(coupons)
                || coupons.size() != kafkaMessage.getIds().size()) {
            log.error("Can Not Find Right Coupon Info: {}",
                    JSON.toJSONString(kafkaMessage));
            // TODO 发送邮件
            return;
        }

        coupons.forEach(c -> c.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}",
                couponDao.saveAll(coupons).size());
    }
}
