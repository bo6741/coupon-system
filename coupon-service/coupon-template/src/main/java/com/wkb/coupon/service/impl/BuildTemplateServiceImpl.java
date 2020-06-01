package com.wkb.coupon.service.impl;

import com.wkb.coupon.dao.CouponTemplateDao;
import com.wkb.coupon.entity.CouponTemplate;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.service.IAsyncService;
import com.wkb.coupon.service.IBuildTemplateService;
import com.wkb.coupon.vo.TempCreateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 构建优惠券模板接口实现
 * @author WuKB
 * @Date 2020/5/17
 */
@Slf4j
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {
    /**异步服务 */
    private final IAsyncService asyncService;

    /**Dao层 */
    private final CouponTemplateDao templateDao;

    @Autowired
    public BuildTemplateServiceImpl(IAsyncService asyncService, CouponTemplateDao templateDao) {
        this.asyncService = asyncService;
        this.templateDao = templateDao;
    }

    /**
     * 创建优惠券模板
     * @param req {@link TempCreateReq}
     * @return
     * @throws ServiceException
     */
    @Override
    public CouponTemplate buildTemplate(TempCreateReq req) throws ServiceException {
        // 参数合法性校验
        if (!req.validate()) {
            throw new ServiceException("Illegal Param in buildTemplate!");
        }

        // 判断同名的优惠券模板是否存在
        if (null != templateDao.findByName(req.getName())) {
            throw new ServiceException("Exist Same Name Template!");
        }

        //构造模板实体类
        CouponTemplate template = new CouponTemplate(
                req.getName(),
                req.getLogo(),
                req.getDesc(),
                req.getCategory(),
                req.getProductLine(),
                req.getCount(),
                req.getUserId(),
                req.getTarget(),
                req.getRule()
        );

        /**
         * template没有id时 jpa的save是保存到数据库
         * 返回的参数是带id的对象
         */
        template = templateDao.save(template);

        // 根据优惠券模板异步生成优惠券码
        asyncService.asyncConstructCouponByTemplate(template);

        return template;
    }
}
