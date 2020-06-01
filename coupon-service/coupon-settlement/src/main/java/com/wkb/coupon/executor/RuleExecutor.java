package com.wkb.coupon.executor;

import com.wkb.coupon.constant.RuleFlag;
import com.wkb.coupon.vo.SettlementInfo;

/**
 * 优惠券规则处理器
 * @author WuKB
 * @Date 2020/5/25
 */
public interface RuleExecutor {

    /**
     * 规则类型标记
     * @return
     */
    RuleFlag ruleConfig();


    /**
     *优惠券规则计算
     * @param settlement
     * @return
     */
    SettlementInfo computeRule(SettlementInfo settlement);
}
