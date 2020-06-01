package com.wkb.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * kafka消息对象
 * @author WuKB
 * @Date 2020/5/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponKafkaMessage {
    /** 优惠券状态 */
    private Integer status;

    /** Coupon 主键 */
    private List<Integer> ids;
}
