package com.wkb.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wkb.coupon.entity.Coupon;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author WuKB
 * @Date 2020/5/19
 */
public class CouponSerialize extends JsonSerializer<Coupon> {

    @Override
    public void serialize(Coupon coupon, JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        // 开始序列化
        generator.writeStartObject();

        generator.writeStringField("id", coupon.getId().toString());
        generator.writeStringField("templateId",
                coupon.getTemplateId().toString());
        generator.writeStringField("userId",
                coupon.getUserId().toString());
        generator.writeStringField("couponCode",
                coupon.getCouponCode());
        generator.writeStringField("assignTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(coupon.getAssignTime()));
        generator.writeStringField("name",
                coupon.getTemplateVo().getName());
        generator.writeStringField("logo",
                coupon.getTemplateVo().getLogo());
        generator.writeStringField("desc",
                coupon.getTemplateVo().getDesc());
        generator.writeStringField("expiration",
                JSON.toJSONString(
                        coupon.getTemplateVo().getRule().getExpiration()));
        generator.writeStringField("discount",
                JSON.toJSONString(
                        coupon.getTemplateVo().getRule().getDiscount()));
        generator.writeStringField("usage",
                JSON.toJSONString(coupon.getTemplateVo().getRule().getUsage()));

        // 结束序列化
        generator.writeEndObject();
    }
}
