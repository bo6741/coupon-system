package com.wkb.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则类型枚举
 * @author WuKB
 * @Date 2020/5/25
 */
@AllArgsConstructor
@Getter
public enum RuleFlag {
    //单类别规则
    FULL_CUT_RULE("满减券规则", "1001"),
    DISCOUNT_RULE("折扣券规则", "1002"),
    DIRECT_CUT_RULE("立减券规则", "1003"),

    //多类别规则
    FULL_DISCOUNT_RULE("满减券 + 折扣券规则", "1004");

    private String desc;

    private String code;

    public static RuleFlag getByCode(String code){
        for (RuleFlag rule: RuleFlag.values()) {
            if (code.equals(rule.getCode())){
                return rule;
            }
        }
        throw new IllegalArgumentException("不存在code为" + code +"的RuleFlag!");
    }
}
