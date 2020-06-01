package com.wkb.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WuKB
 * @Date 2020/5/13
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class RateLimiterFilter extends AbstractPreZuulFilter {

    /** 每秒可以获取到200个令牌  即每秒只限制处理200个请求*/
    RateLimiter rateLimiter = RateLimiter.create(200.0);

    @Override
    protected Object cRun() {

        HttpServletRequest request = context.getRequest();

        if (rateLimiter.tryAcquire()) {
            log.info("get rate token success");
            return success();
        } else {
            log.error("rate limit: {}", request.getRequestURI());
            return fail(402, "error: rate limit");
        }
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return 2;
    }
}
