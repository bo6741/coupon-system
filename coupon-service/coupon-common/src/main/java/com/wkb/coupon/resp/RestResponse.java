package com.wkb.coupon.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author WuKB
 * @Date 2020/5/14
 */
@Data
@AllArgsConstructor
public class RestResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public RestResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
