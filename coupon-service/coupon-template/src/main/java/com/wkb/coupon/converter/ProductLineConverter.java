package com.wkb.coupon.converter;

import com.wkb.coupon.constant.ProductLineE;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 产品线枚举属性转换器
 * @author WuKB
 * @Date 2020/5/16
 */
@Converter
public class ProductLineConverter implements AttributeConverter<ProductLineE, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductLineE productLineE) {
        return productLineE.getCode();
    }

    @Override
    public ProductLineE convertToEntityAttribute(Integer code) {
        return ProductLineE.getByCode(code);
    }
}
