package com.wkb.coupon.constant;

/**
 * @author WuKB
 * @Date 2020/5/17
 * 常量定义类
 */
public class Constant {

    /**
     * kafka 消息Topic
     */
    public static final String TOPIC = "user_coupon_op";

    public static class RedisPrefix {

        /** 优惠券码 key 前缀 */
        public static final String COUPON_TEMPLATE =
                "coupon_template_code_";

        /** 用户当前所有可用的优惠券 key 前缀 */
        public static final String USER_COUPON_USABLE =
                "user_coupon_usable_";

        /** 用户当前所有已使用的优惠券 key 前缀 */
        public static final String USER_COUPON_USED =
                "user_coupon_used_";

        /** 用户当前所有已过期的优惠券 key 前缀 */
        public static final String USER_COUPON_EXPIRED =
                "user_coupon_expired_";
    }
}
