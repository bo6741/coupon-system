package com.wkb.coupon.dao;

import com.wkb.coupon.constant.CouponStatus;
import com.wkb.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户优惠券dao定义
 * JpaRepository<实体类, Id类型>
 * @author WuKB
 * @Date 2020/5/19
 */
public interface CouponDao extends JpaRepository<Coupon, Integer> {

    /**
     * 根据userId、状态查找优惠券记录
     * @param userId
     * @param status
     * @return
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
