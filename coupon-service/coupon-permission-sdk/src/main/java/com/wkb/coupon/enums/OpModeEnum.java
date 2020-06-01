package com.wkb.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 * @author WuKB
 * @Date 2020/6/1
 */
@Getter
@AllArgsConstructor
public enum OpModeEnum {
    READ("读"),
    WRITE("写");

    private String mode;
}
