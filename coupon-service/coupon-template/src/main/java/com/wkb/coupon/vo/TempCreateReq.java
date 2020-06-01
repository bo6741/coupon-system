package com.wkb.coupon.vo;

import com.wkb.coupon.constant.CouponCategory;
import com.wkb.coupon.constant.DistributeTarget;
import com.wkb.coupon.constant.ProductLineE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 创建优惠券模板请求参数
 * @author WuKB
 * @Date 2020/5/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempCreateReq {
    /** 优惠券名称 */
    private String name;

    /** 优惠券 logo */
    private String logo;

    /** 优惠券描述 */
    private String desc;

    /** 优惠券分类 */
    private String category;

    /** 产品线 */
    private Integer productLine;

    /** 总数 */
    private Integer count;

    /** 创建用户 */
    private Long userId;

    /** 目标用户 */
    private Integer target;

    /** 优惠券规则 */
    private TemplateRule rule;

    /**
     * <h2>校验对象的合法性</h2>
     * */
    public boolean validate() {

        boolean stringValid = StringUtils.isNotEmpty(name)
                && StringUtils.isNotEmpty(logo)
                && StringUtils.isNotEmpty(desc);
        boolean enumValid = null != CouponCategory.getByCode(category)
                && null != ProductLineE.getByCode(productLine)
                && null != DistributeTarget.getByCode(target);
        boolean numValid = count > 0 && userId > 0;

        return stringValid && enumValid && numValid && rule.validate();
    }
}
