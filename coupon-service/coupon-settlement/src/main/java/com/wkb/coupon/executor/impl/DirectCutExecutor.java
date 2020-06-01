package com.wkb.coupon.executor.impl;

import com.wkb.coupon.constant.RuleFlag;
import com.wkb.coupon.executor.AbstractExecutor;
import com.wkb.coupon.executor.RuleExecutor;
import com.wkb.coupon.vo.CouponTemplateVo;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 立减优惠券结算执行器
 * @author WuKB
 * @Date 2020/5/25
 */
@Component
@Slf4j
public class DirectCutExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.DIRECT_CUT_RULE;
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
            log.debug("商品类型与满减优惠券不匹配!");
            return probability;
        }

        // 立减优惠券直接使用, 没有门槛
        CouponTemplateVo templateSDK = settlement.getCouponAndTemplateInfos()
                .get(0).getTemplate();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        // 计算使用优惠券之后的价格 - 结算
        settlement.setCost(
                retain2Decimals(goodsSum - quota) > minCost() ?
                        retain2Decimals(goodsSum - quota) : minCost()
        );

        log.debug("Use LiJian Coupon Make Goods Cost From {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }
}
