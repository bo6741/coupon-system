package com.wkb.coupon.constant;

import com.wkb.coupon.utils.AssertUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * 产品线枚举
 * @author WuKB
 * @Date 2020/5/16
 */
@Getter
@AllArgsConstructor
public enum ProductLineE {
    JING_XI(618, "京西"),
    TAO_ZHUAN(111, "淘砖");

    /**
     * 产品线编码
     */
    private Integer code;

    /**
     * 产品线描述
     */
    private String desc;


    public static ProductLineE getByCode(Integer code){
        AssertUtils.isTure(null != code, "code is null!");

        return Stream.of(values())
                .filter(category -> category.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists !"));
    }
}
