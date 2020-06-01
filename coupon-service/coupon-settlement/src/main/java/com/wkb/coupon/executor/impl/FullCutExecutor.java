package com.wkb.coupon.executor.impl;

import com.wkb.coupon.constant.RuleFlag;
import com.wkb.coupon.executor.AbstractExecutor;
import com.wkb.coupon.executor.RuleExecutor;
import com.wkb.coupon.vo.CouponTemplateVo;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 满减优惠券结算执行器
 * @author WuKB
 * @Date 2020/5/25
 */
@Component
@Slf4j
public class FullCutExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.FULL_CUT_RULE;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(
                goodsCostSum(settlement.getGoodsInfos())
        );
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );
        if (null != probability) {
            log.debug("优惠券不符合商品类型!");
            return probability;
        }

        // 判断满减是否符合折扣标准
        CouponTemplateVo templateSDK = settlement.getCouponAndTemplateInfos()
                .get(0).getTemplate();
        double base = (double) templateSDK.getRule().getDiscount().getBase();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        // 如果不符合标准, 则直接返回商品总价
        if (goodsSum < base) {
            log.debug("商品总价未满" + base + "!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }

        // 计算使用优惠券之后的价格 - 结算
        settlement.setCost(retain2Decimals(
                (goodsSum - quota) > minCost() ? (goodsSum - quota) : minCost()
        ));
        log.debug("使用了满减优惠券 {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }
}
