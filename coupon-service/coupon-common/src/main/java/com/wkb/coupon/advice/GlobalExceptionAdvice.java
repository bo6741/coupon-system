package com.wkb.coupon.advice;

import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.resp.RestResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WuKB
 * @Date 2020/5/14
 * 统一异常处理
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 对抛出ServiceException统一处理
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    public RestResponse<String> handlerCouponException(HttpServletRequest req, ServiceException ex) {
        RestResponse<String> response = new RestResponse(-1, "business error");
        response.setData(ex.getMessage());
        return response;
    }
}
