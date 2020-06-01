package com.wkb.coupon.feign.hystrix;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.feign.TemplateClient;
import com.wkb.coupon.resp.RestResponse;
import com.wkb.coupon.vo.CouponTemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * coupon-template 微服务熔断服务降级
 * @author WuKB
 * @Date 2020/5/23
 */
@Slf4j
@Component
public class TemplateClientHystrix implements TemplateClient {
    @Override
    public RestResponse<List<CouponTemplateVo>> findAllUsableTemplate() {
        log.error("[eureka-client-coupon-template] findAllUsableTemplate request error");
        return new RestResponse<>
                (
                    -1,
                    "[eureka-client-coupon-template] findAllUsableTemplate request error",
                    Collections.emptyList()
                );
    }

    @Override
    public RestResponse<Map<Integer, CouponTemplateVo>> findIds2TemplateSDK(Collection<Integer> ids) {
        log.error("[eureka-client-coupon-template] findIds2TemplateSDK" +
                "request error, params is : [{}]", JSON.toJSONString(ids));

        return new RestResponse<>
                (
                -1,
                "[eureka-client-coupon-template] request error",
                new HashMap<>()
                );
    }
}
