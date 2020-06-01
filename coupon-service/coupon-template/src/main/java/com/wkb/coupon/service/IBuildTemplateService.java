package com.wkb.coupon.service;

import com.wkb.coupon.entity.CouponTemplate;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.vo.TempCreateReq;

/**
 * @author WuKB
 * @Date 2020/5/16
 */
public interface IBuildTemplateService {
    /**
     * 创建优惠券模板
     * @param req {@link TempCreateReq}
     * @return {@link CouponTemplate}
     * @throws ServiceException
     */
    CouponTemplate buildTemplate(TempCreateReq req) throws ServiceException;
}
