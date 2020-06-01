package com.wkb.coupon.converter;

import com.wkb.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 优惠券分类枚举属性转换器
 * AttributeConverter<X, Y>
 * X: 实体类属性
 * Y：存储到数据库里的属性
 * @author WuKB
 * @Date 2020/5/16
 */
@Converter
public class CouponCategoryConverter implements AttributeConverter<CouponCategory, String> {

    /**
     * 存储入数据库的转换
     * @param category
     * @return
     */
    @Override
    public String convertToDatabaseColumn(CouponCategory category) {
        return category.getCode();
    }

    /**
     * 查询出库的转换
     * @param s
     * @return
     */
    @Override
    public CouponCategory convertToEntityAttribute(String s) {
        return CouponCategory.getByCode(s);
    }
}
