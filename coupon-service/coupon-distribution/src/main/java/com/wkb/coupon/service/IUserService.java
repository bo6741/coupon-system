package com.wkb.coupon.service;

import com.wkb.coupon.entity.Coupon;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.vo.AcquireTemplateRequest;
import com.wkb.coupon.vo.CouponTemplateVo;
import com.wkb.coupon.vo.SettlementInfo;

import java.util.List;

/**
 * 用户服务接口定义
 * @author WuKB
 * @Date 2020/5/19
 */
public interface IUserService {
    /**
     * <h2>根据用户 id 和状态查询优惠券记录</h2>
     * @param userId 用户 id
     * @param status 优惠券状态
     * @return {@link Coupon}s
     * */
    List<Coupon> findCouponsByStatus(Long userId, Integer status)
            throws ServiceException;

    /**
     * <h2>根据用户 id 查找当前可以领取的优惠券模板</h2>
     * 调用coupon-template微服务实现
     * @param userId 用户 id
     * @return {@link CouponTemplateVo}s
     * */
    List<CouponTemplateVo> findAvailableTemplate(Long userId)
            throws ServiceException;

    /**
     * <h2>用户领取优惠券</h2>
     * @param request {@link AcquireTemplateRequest}
     * @return {@link Coupon}
     * */
    Coupon acquireTemplate(AcquireTemplateRequest request)
            throws ServiceException;

    /**
     * <h2>结算(核销)优惠券</h2>
     * 调用 coupon-settlement微服务实现
     * @param info {@link SettlementInfo}
     * @return {@link SettlementInfo}
     * */
    SettlementInfo settlement(SettlementInfo info) throws ServiceException;
}
