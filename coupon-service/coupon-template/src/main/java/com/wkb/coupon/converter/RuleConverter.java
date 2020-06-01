package com.wkb.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.vo.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 优惠券规则属性转换器
 * @author WuKB
 * @Date 2020/5/16
 */
@Converter
public class RuleConverter implements AttributeConverter<TemplateRule, String> {
    @Override
    public String convertToDatabaseColumn(TemplateRule templateRule) {
        return JSON.toJSONString(templateRule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String s) {
        return JSON.parseObject(s, TemplateRule.class);
    }
}
