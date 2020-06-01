package com.wkb.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品信息 mock
 * @author WuKB
 * @Date 2020/5/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo {
    /** 商品类型*/
    private Integer type;

    /** 商品价格 */
    private Double price;

    /** 商品数量 */
    private Integer count;

    // TODO 名称, 使用信息
}
