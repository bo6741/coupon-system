package com.wkb.coupon.service;

import com.wkb.coupon.entity.Coupon;
import com.wkb.coupon.exception.ServiceException;

import java.util.List;

/**
 * @author WuKB
 * @Date 2020/5/19
 */
public interface IRedisService {

    /**
     * 根据用户id、状态 查找 Redis缓存的优惠券列表数据
     * @param userId
     * @param status
     * @return
     */
    List<Coupon> getCachedCoupons(Long userId, Integer status);


    /**
     * <h2>保存空的优惠券列表到缓存中</h2>
     * @param userId 用户 id
     * @param status 优惠券状态列表
     * */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    /**
     * <h2>尝试从 Cache 中获取一个优惠券码</h2>
     * @param templateId 优惠券模板主键
     * @return 优惠券码
     * */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * <h2>将优惠券保存到 Cache 中</h2>
     * @param userId 用户 id
     * @param coupons {@link Coupon}s
     * @param status 优惠券状态
     * @return 保存成功的个数
     * */
    Integer addCouponToCache(Long userId, List<Coupon> coupons,
                             Integer status) throws ServiceException;
}
