package com.wkb.coupon.constant;

import com.wkb.coupon.utils.AssertUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * 优惠券分类枚举
 * @author WuKB
 * @Date 2020/5/16
 */
@Getter
@AllArgsConstructor
public enum CouponCategory {
    FULL_CUT_COUPON("满减券", "1001"),
    DISCOUNT_COUPON("折扣券", "1002"),
    DIRECT_CUT_COUPON("立减券", "1003");

    /**
     * 优惠券描述
     */
    private String desc;

    /**
     * 优惠券编码
     */
    private String code;

    public static CouponCategory getByCode(String code){
        AssertUtils.isTure(StringUtils.isNotBlank(code), "code is blank!");

        return Stream.of(values())
                .filter(category -> category.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists !"));
    }
}
