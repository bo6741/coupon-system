package com.wkb.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author WuKB
 * @Date 2020/5/16
 */
@Getter
@AllArgsConstructor
public enum DistributeTarget {
    SINGLE("单用户", 1),
    MULTI("多用户", 2);

    /** 分发目标描述 */
    private String description;

    /** 分发目标编码 */
    private Integer code;

    public static DistributeTarget getByCode(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }

}
