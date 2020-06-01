package com.wkb.coupon.feign;

import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.feign.hystrix.SettlementClientHystrix;
import com.wkb.coupon.resp.RestResponse;
import com.wkb.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 优惠券结算微服务
 * @author WuKB
 * @Date 2020/5/23
 */
@FeignClient(value = "eureka-client-coupon-settlement",
        fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    /**
     * 优惠券规则计算
     * @param settlement
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/coupon-settlement/settlement/compute",
            method = RequestMethod.POST)
    RestResponse<SettlementInfo> computeRule(
            @RequestBody SettlementInfo settlement) throws ServiceException;
}
