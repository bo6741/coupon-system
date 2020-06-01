package com.wkb.coupon.filter;

import org.springframework.stereotype.Component;

/**
 * @author WuKB
 * @Date 2020/5/14
 */
@Component
public class PreRequestFilter extends AbstractPreZuulFilter {
    @Override
    protected Object cRun() {
        context.set("startTime", System.currentTimeMillis());

        return success();
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
