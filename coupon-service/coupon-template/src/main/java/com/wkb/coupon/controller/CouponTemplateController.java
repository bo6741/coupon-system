package com.wkb.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.entity.CouponTemplate;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.service.IBuildTemplateService;
import com.wkb.coupon.service.ITemplateBaseService;
import com.wkb.coupon.vo.CouponTemplateVo;
import com.wkb.coupon.vo.TempCreateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author WuKB
 * @Date 2020/5/17
 */
@Slf4j
@RestController
public class CouponTemplateController {
    /** 构建优惠券模板服务 */
    private final IBuildTemplateService buildTemplateService;

    /** 优惠券模板基础服务 */
    private final ITemplateBaseService templateBaseService;

    @Autowired
    public CouponTemplateController(IBuildTemplateService buildTemplateService,
                                    ITemplateBaseService templateBaseService) {
        this.buildTemplateService = buildTemplateService;
        this.templateBaseService = templateBaseService;
    }

    /**
     * <h2>构建优惠券模板</h2>
     * 127.0.0.1:7001/coupon-template/template/build
     * */
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TempCreateReq request)
            throws ServiceException {
        log.info("Build Template: {}", JSON.toJSONString(request));
        return buildTemplateService.buildTemplate(request);
    }

    /**
     * <h2>构造优惠券模板详情</h2>
     * 127.0.0.1:7001/coupon-template/template/info?id=1
     * */
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id)
            throws ServiceException {
        log.info("Build Template Info For: {}", id);
        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     * <h2>查找所有可用的优惠券模板</h2>
     * 127.0.0.1:7001/coupon-template/template/sdk/all
     * */
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateVo> findAllUsableTemplate() {
        log.info("Find All Usable Template.");
        return templateBaseService.findAllUsableTemplate();
    }

    /**
     * <h2>获取模板 ids 到 CouponTemplateSDK 的映射</h2>
     * 127.0.0.1:7001/coupon-template/template/sdk/infos
     * */
    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateVo> findIds2TemplateSDK(
            @RequestParam("ids") Collection<Integer> ids
    ) {
        log.info("FindIds2TemplateSDK: {}", JSON.toJSONString(ids));
        return templateBaseService.findIds2TemplateSDK(ids);
    }
}
