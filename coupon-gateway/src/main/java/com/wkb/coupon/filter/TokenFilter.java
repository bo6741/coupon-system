package com.wkb.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WuKB
 * @Date 2020/5/13
 */
@Slf4j
@Component
public class TokenFilter extends AbstractPreZuulFilter{
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s",
                request.getMethod(), request.getRequestURL().toString()));

        Object token = request.getParameter("token");
        if (null == token) {
            log.error("用户未登录！");
            return fail(401, "用户未登录！");
        }

        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
