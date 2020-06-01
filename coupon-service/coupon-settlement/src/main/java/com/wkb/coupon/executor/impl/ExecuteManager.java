package com.wkb.coupon.executor.impl;

import com.wkb.coupon.constant.RuleFlag;
import com.wkb.coupon.executor.RuleExecutor;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券结算规则执行管理器 ： 策略模式
 * @author WuKB
 * @Date 2020/5/28
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class ExecuteManager {

    private Map<RuleFlag, RuleExecutor> executorMap = new HashMap<RuleFlag, RuleExecutor>();

    public ExecuteManager(){
        executorMap.put(RuleFlag.DIRECT_CUT_RULE, new DirectCutExecutor());
        executorMap.put(RuleFlag.DISCOUNT_RULE, new DiscountExecutor());
        executorMap.put(RuleFlag.FULL_CUT_RULE, new FullCutExecutor());
        executorMap.put(RuleFlag.FULL_DISCOUNT_RULE, new FullDiscountExecutor());
    }

    /**
     * 结算
     * @param settlement
     * @return
     */
    public SettlementInfo computeRule(SettlementInfo settlement){
        if (settlement.getCouponAndTemplateInfos().size() == 1){
            RuleExecutor executor = executorMap.get(RuleFlag.getByCode(settlement.getCouponAndTemplateInfos().get(0).
                    getTemplate().getCategory()));
            return executor.computeRule(settlement);
        }else {
            return executorMap.get(RuleFlag.FULL_DISCOUNT_RULE).computeRule(settlement);
        }
    }

}

