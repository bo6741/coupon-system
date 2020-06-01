package com.wkb.coupon.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取优惠券请求对象
 * @author WuKB
 * @Date 2020/5/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcquireTemplateRequest {
    /** 用户 id */
    private Long userId;

    /** 优惠券模板信息 */
    private CouponTemplateVo templateVo;
}
