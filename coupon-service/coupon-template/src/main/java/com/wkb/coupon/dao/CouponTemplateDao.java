package com.wkb.coupon.dao;

import com.wkb.coupon.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 优惠券模板 dao定义
 * @author WuKB
 * @Date 2020/5/16
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer> {
    /**
     * 根据模板名称查询模板
     * @param name
     * @return
     */
    CouponTemplate findByName(String name);

    /**
     * 根据available、expired查找模板
     * @param available
     * @param expired
     * @return
     */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);

    /**
     * 根据 expired 标记查找模板记录
     * where expired = ...
     * */
    List<CouponTemplate> findAllByExpired(Boolean expired);
}
