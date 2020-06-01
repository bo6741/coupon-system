package com.wkb.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.entity.Coupon;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.service.IUserService;
import com.wkb.coupon.vo.AcquireTemplateRequest;
import com.wkb.coupon.vo.CouponTemplateVo;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WuKB
 * @Date 2020/5/24
 */
@Slf4j
@RestController
public class DistributionController {

    @Autowired
    private IUserService userService;

    /**
     * 根据用户 id 和优惠券状态查找用户优惠券记录
     * */
    @GetMapping("/findCouponsByStatus")
    public List<Coupon> findCouponsByStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("status") Integer status) throws ServiceException {

        log.info("根据用户 id 和优惠券状态查找用户优惠券记录: {}, {}", userId, status);
        return userService.findCouponsByStatus(userId, status);
    }

    /**
     * 根据用户 id 查找当前可以领取的优惠券模板
     * */
    @GetMapping("/findAvailableTemplate")
    public List<CouponTemplateVo> findAvailableTemplate(
            @RequestParam("userId") Long userId) throws ServiceException {

        log.info("根据用户 id 查找当前可以领取的优惠券模板: {}", userId);
        return userService.findAvailableTemplate(userId);
    }

    /**
     * 用户领取优惠券
     * */
    @PostMapping("/acquire/template")
    public Coupon acquireTemplate(@RequestBody AcquireTemplateRequest request)
            throws ServiceException {

        log.info("用户领取优惠券: {}", JSON.toJSONString(request));
        return userService.acquireTemplate(request);
    }

    /**
     * <h2>结算(核销)优惠券</h2>
     * */
    @PostMapping("/settlement")
    public SettlementInfo settlement(@RequestBody SettlementInfo info)
            throws ServiceException {

        log.info("Settlement: {}", JSON.toJSONString(info));
        return userService.settlement(info);
    }
}
