package com.wkb.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 用户优惠券状态
 * @author WuKB
 * @Date 2020/5/19
 */
@Getter
@AllArgsConstructor
public enum  CouponStatus {
    USABLE("可用的", 1),
    USED("已使用的", 2),
    EXPIRED("过期的（未使用的）", 3);

    private String desc;

    private Integer code;

    public static CouponStatus getByCode(Integer code){
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists"));
    }
}
