package com.wkb.coupon.exception;

/**
 * @author WuKB
 * @Date 2020/5/14
 * 服务异常
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }
}
