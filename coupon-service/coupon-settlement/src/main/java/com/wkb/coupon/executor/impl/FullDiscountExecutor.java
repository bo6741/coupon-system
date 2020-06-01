package com.wkb.coupon.executor.impl;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.constant.CouponCategory;
import com.wkb.coupon.constant.RuleFlag;
import com.wkb.coupon.executor.AbstractExecutor;
import com.wkb.coupon.executor.RuleExecutor;
import com.wkb.coupon.vo.GoodsInfo;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 满减 + 折扣优惠券执行器
 * @author WuKB
 * @Date 2020/5/28
 */
@Slf4j
@Component
public class FullDiscountExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.FULL_DISCOUNT_RULE;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(goodsCostSum(
                settlement.getGoodsInfos()
        ));
        // 商品类型的校验
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );
        if (null != probability) {
            log.debug("ManJian And ZheKou Template Is Not Match To GoodsType!");
            return probability;
        }

        SettlementInfo.CouponAndTemplateInfo fullCut = null;
        SettlementInfo.CouponAndTemplateInfo discount = null;

        for (SettlementInfo.CouponAndTemplateInfo ct :
                settlement.getCouponAndTemplateInfos()) {
            if (CouponCategory.getByCode(ct.getTemplate().getCategory()) ==
                    CouponCategory.FULL_CUT_COUPON) {
                fullCut = ct;
            } else {
                discount = ct;
            }
        }

        assert null != fullCut;
        assert null != discount;

        // 当前的折扣优惠券和满减券如果不能共用(一起使用), 清空优惠券, 返回商品原价
        if (!isTemplateCanShared(fullCut, discount)) {
            log.debug("当前满减券和折扣券不能共用!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }

        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        double manJianBase = (double) fullCut.getTemplate().getRule()
                .getDiscount().getBase();
        double manJianQuota = (double) fullCut.getTemplate().getRule()
                .getDiscount().getQuota();

        // 最终的价格
        double targetSum = goodsSum;

        // 先计算满减
        if (targetSum >= manJianBase) {
            targetSum -= manJianQuota;
            ctInfos.add(fullCut);
        }

        // 再计算折扣
        double zheKouQuota = (double) discount.getTemplate().getRule()
                .getDiscount().getQuota();
        targetSum *= zheKouQuota * 1.0 / 100;
        ctInfos.add(discount);

        settlement.setCouponAndTemplateInfos(ctInfos);
        settlement.setCost(retain2Decimals(
                targetSum > minCost() ? targetSum : minCost()
        ));

        log.debug("使用了满减券和折扣券 from {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }

    /**
     * 检验商品类型和优惠券类型是否匹配
     * @param settlement
     * @return
     */
    @Override
    @SuppressWarnings("all")
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement) {
        List<Integer> goodsType = settlement.getGoodsInfos().stream()
                .map(GoodsInfo::getType).collect(Collectors.toList());
        List<Integer> templateGoodsType = new ArrayList<>();

        settlement.getCouponAndTemplateInfos().forEach(ct -> {

            templateGoodsType.addAll(JSON.parseObject(
                    ct.getTemplate().getRule().getUsage().getGoodsType(),
                    List.class
            ));

        });

        // 如果想要使用多类优惠券, 则必须要所有的商品类型都包含在内, 即差集为空
        return CollectionUtils.isEmpty(CollectionUtils.subtract(
                goodsType, templateGoodsType
        ));
    }

    /**
     * 当前的两张优惠券是否可以共用
     * 即校验 TemplateRule 中的 weight 是否满足条件
     * */
    @SuppressWarnings("all")
    private boolean
    isTemplateCanShared(SettlementInfo.CouponAndTemplateInfo fullCut,
                        SettlementInfo.CouponAndTemplateInfo discount) {

        String manjianKey = fullCut.getTemplate().getKey()
                + String.format("%04d", fullCut.getTemplate().getId());
        String zhekouKey = discount.getTemplate().getKey()
                + String.format("%04d", discount.getTemplate().getId());

        List<String> allSharedKeysForManjian = new ArrayList<>();
        allSharedKeysForManjian.add(manjianKey);
        allSharedKeysForManjian.addAll(JSON.parseObject(
                fullCut.getTemplate().getRule().getWeight(),
                List.class
        ));

        List<String> allSharedKeysForZhekou = new ArrayList<>();
        allSharedKeysForZhekou.add(zhekouKey);
        allSharedKeysForZhekou.addAll(JSON.parseObject(
                discount.getTemplate().getRule().getWeight(),
                List.class
        ));

        return CollectionUtils.isSubCollection(
                Arrays.asList(manjianKey, zhekouKey), allSharedKeysForManjian)
                || CollectionUtils.isSubCollection(
                Arrays.asList(manjianKey, zhekouKey), allSharedKeysForZhekou
        );
    }
}
