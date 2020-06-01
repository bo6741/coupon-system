package com.wkb.coupon.utils;

import com.wkb.coupon.constant.CouponStatus;
import com.wkb.coupon.constant.PeriodType;
import com.wkb.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优惠券分类工具类
 * @author WuKB
 * @Date 2020/5/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponClassify {
    /** 可以使用的 */
    private List<Coupon> usable;

    /** 已使用的 */
    private List<Coupon> used;

    /** 已过期的 */
    private List<Coupon> expired;

    /**
     * <h2>对当前的优惠券进行分类</h2>
     * */
    public static CouponClassify classify(List<Coupon> coupons) {

        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {

            // 判断优惠券是否过期
            boolean isTimeExpire;
            long curTime = new Date().getTime();

            if (c.getTemplateVo().getRule().getExpiration().getPeriod().equals(
                    PeriodType.REGULAR.getCode()
            )) {
                isTimeExpire = c.getTemplateVo().getRule().getExpiration()
                        .getDeadline() <= curTime;
            } else {
                isTimeExpire = DateUtils.addDays(
                        c.getAssignTime(),
                        c.getTemplateVo().getRule().getExpiration().getGap()
                ).getTime() <= curTime;
            }

            if (c.getStatus() == CouponStatus.USED) {
                used.add(c);
            } else if (c.getStatus() == CouponStatus.EXPIRED || isTimeExpire) {
                expired.add(c);
            } else {
                usable.add(c);
            }
        });

        return new CouponClassify(usable, used, expired);
    }
}
