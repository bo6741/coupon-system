package com.wkb.coupon.service.impl;

import com.wkb.coupon.dao.CouponTemplateDao;
import com.wkb.coupon.entity.CouponTemplate;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.service.ITemplateBaseService;
import com.wkb.coupon.vo.CouponTemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WuKB
 * @Date 2020/5/17
 */
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private final CouponTemplateDao templateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * <h2>根据优惠券模板 id 获取优惠券模板信息</h2>
     * @param id 模板 id
     * @return {@link CouponTemplate} 优惠券模板实体
     * */
    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws ServiceException {
        Optional<CouponTemplate> template = templateDao.findById(id);
        if (!template.isPresent()){
            throw new ServiceException(id + " Template not found!");
        }
        return template.get();
    }

    /**
     * <h2>查找所有可用的优惠券模板</h2>
     * @return {@link CouponTemplateVo}s
     * */
    @Override
    public List<CouponTemplateVo> findAllUsableTemplate() {

        //查找有效 且未过期的优惠券
        List<CouponTemplate> templateList = templateDao.findAllByAvailableAndExpired(true, false);

        return templateList.stream().map(this::template2TemplateVo).collect(Collectors.toList());
    }

    /**
     * <h2>获取模板 ids 到 CouponTemplateSDK 的映射</h2>
     * @param ids 模板 ids
     * @return Map<key: 模板 id， value: CouponTemplateSDK>
     * */
    @Override
    public Map<Integer, CouponTemplateVo> findIds2TemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> templateList = templateDao.findAllById(ids);

        return templateList.stream().map(this::template2TemplateVo).
                collect(Collectors.toMap(CouponTemplateVo::getId, Function.identity()));
    }

    /**
     * <h2>将 CouponTemplate 转换为 CouponTemplateSDK</h2>
     * */
    private CouponTemplateVo template2TemplateVo(CouponTemplate template) {

        return new CouponTemplateVo(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getDesc(),
                template.getCategory().getCode(),
                template.getProductLine().getCode(),
                template.getKey(),  // 并不是拼装好的 Template Key
                template.getTarget().getCode(),
                template.getRule()
        );
    }
}
