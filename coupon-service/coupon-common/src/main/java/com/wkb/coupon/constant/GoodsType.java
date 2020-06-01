package com.wkb.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品类型枚举
 * @author WuKB
 * @Date 2020/5/19
 */
@Getter
@AllArgsConstructor
public enum GoodsType {
    WENYU("文娱", 1),
    SHENGXIAN("生鲜", 2),
    JIAJU("家居", 3),
    OTHERS("其他", 4),
    ALL("全品类", 5);

    /** 商品类型描述 */
    private String description;

    /** 商品类型编码 */
    private Integer code;

    public static GoodsType getByCode(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(code + " not exists!")
                );
    }
}
