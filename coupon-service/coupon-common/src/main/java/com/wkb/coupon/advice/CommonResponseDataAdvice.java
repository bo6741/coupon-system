package com.wkb.coupon.advice;

import com.wkb.coupon.annotation.IgnoreResponseAdvice;
import com.wkb.coupon.resp.RestResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author WuKB
 * @Date 2020/5/14
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要对响应进行增强处理
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //判断方法以及所在的类是否加上了 @IgnoreResponseAdvice注解
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)
        || methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }

        //对响应增强处理 会执行beforeBodyWrite
        return true;
    }

    /**
     * 返回结果之前先进行处理
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        RestResponse<Object> resp = new RestResponse(0, "");

        if (null == o){
            return resp;
        }else if (o instanceof RestResponse){
            resp = (RestResponse<Object>)o;
        }else {
            resp.setData(o);
        }

        return resp;
    }
}
