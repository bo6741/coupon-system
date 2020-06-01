package com.wkb.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Http方法类型枚举
 * @author WuKB
 * @Date 2020/6/1
 */
@AllArgsConstructor
@Getter
public enum HttpMethodEnum {
    GET,
    HEAD,
    POST,
    PUT,
    PATHC,
    DELETE,
    OPTIONS,
    TRACE,
    ALL
}
