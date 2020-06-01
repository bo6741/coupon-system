package com.wkb.coupon.service;

import com.wkb.coupon.entity.CouponTemplate;

/**
 * 异步服务接口
 * @author WuKB
 * @Date 2020/5/17
 */
public interface IAsyncService {
    /**
     * 根据模板异步的创建优惠券码
     * @param template {@link CouponTemplate} 优惠券模板实体
     * */
    void asyncConstructCouponByTemplate(CouponTemplate template);
}
