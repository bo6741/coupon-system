package com.wkb.coupon.utils;

import com.wkb.coupon.exception.ServiceException;

/**
 * @author WuKB
 * @Date 2020/5/16
 * 断言工具类
 */
public class AssertUtils {
    public static void isTure(boolean flag, String message) throws ServiceException {
        if (!flag){
            throw new ServiceException(message);
        }
    }
}
