package com.wkb.coupon.feign;

import com.wkb.coupon.feign.hystrix.TemplateClientHystrix;
import com.wkb.coupon.resp.RestResponse;
import com.wkb.coupon.vo.CouponTemplateVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * coupon-template : Feign 接口定义
 * @author WuKB
 * @Date 2020/5/23
 */
//feignClient的value是被调用的application name
@FeignClient(value = "eureka-client-coupon-template",
        fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    /**
     * 查找所有可用的优惠券模板
     * @return
     */
    @RequestMapping(value = "/coupon-template/template/sdk/all", method = RequestMethod.GET)
    RestResponse<List<CouponTemplateVo>> findAllUsableTemplate();

    /**
     *  获取ids对应的模板
     * @return
     */
    @RequestMapping(value = "/coupon-template/template/sdk/infos", method = RequestMethod.GET)
    RestResponse<Map<Integer, CouponTemplateVo>> findIds2TemplateSDK(@RequestParam("ids") Collection<Integer> ids);
}
