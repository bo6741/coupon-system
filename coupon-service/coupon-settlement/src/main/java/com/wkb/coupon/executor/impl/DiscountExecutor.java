package com.wkb.coupon.executor.impl;

import com.wkb.coupon.constant.RuleFlag;
import com.wkb.coupon.executor.AbstractExecutor;
import com.wkb.coupon.executor.RuleExecutor;
import com.wkb.coupon.vo.CouponTemplateVo;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 折扣优惠券结算执行器
 * @author WuKB
 * @Date 2020/5/25
 */
@Component
@Slf4j
public class DiscountExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.DISCOUNT_RULE;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(goodsCostSum(
                settlement.getGoodsInfos()
        ));
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );
        if (null != probability) {
            log.debug("商品类型与折扣优惠券不匹配!");
            return probability;
        }

        // 折扣优惠券可以直接使用, 没有门槛
        CouponTemplateVo templateSDK = settlement.getCouponAndTemplateInfos()
                .get(0).getTemplate();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        // 计算使用优惠券之后的价格
        settlement.setCost(
                retain2Decimals((goodsSum * (quota * 1.0 / 100))) > minCost() ?
                        retain2Decimals((goodsSum * (quota * 1.0 / 100)))
                        : minCost()
        );
        log.debug("用户使用折扣券 from {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }
}
