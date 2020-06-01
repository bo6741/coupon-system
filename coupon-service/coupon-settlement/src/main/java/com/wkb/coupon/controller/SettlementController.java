package com.wkb.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.executor.impl.ExecuteManager;
import com.wkb.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 结算controller
 * @author WuKB
 * @Date 2020/5/28
 */
@Slf4j
@RestController
public class SettlementController {

    private final ExecuteManager executeManager;

    @Autowired
    public SettlementController(ExecuteManager executeManager) {
        this.executeManager = executeManager;
    }

    @PostMapping("/settlement/compute")
    public SettlementInfo computeRule(@RequestBody SettlementInfo settlement) throws ServiceException{
        log.info("进入方法:{},参数为:{}", "computeRule", JSON.toJSONString(settlement));
        return executeManager.computeRule(settlement);
    }
}
