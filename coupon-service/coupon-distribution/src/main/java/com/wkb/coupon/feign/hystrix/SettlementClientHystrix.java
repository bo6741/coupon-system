package com.wkb.coupon.feign.hystrix;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.feign.SettlementClient;
import com.wkb.coupon.resp.RestResponse;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 结算微服务熔断服务降级
 * @author WuKB
 * @Date 2020/5/23
 */
@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient {

    /**
     * 优惠券计算规则
     * @param settlement
     * @return
     * @throws ServiceException
     */
    @Override
    public RestResponse<SettlementInfo> computeRule(SettlementInfo settlement) throws ServiceException {
        log.error("[eureka-client-coupon-settlement] computeRule" +
                "request error params is [{}]", JSON.toJSONString(settlement));

        settlement.setEmploy(false);
        settlement.setCost(-1.0);

        return new RestResponse<>
                (
                -1,
                "[eureka-client-coupon-settlement] request error",
                settlement
                );
    }
}

