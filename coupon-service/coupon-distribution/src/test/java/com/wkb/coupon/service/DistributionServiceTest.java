package com.wkb.coupon.service;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.constant.CouponStatus;
import com.wkb.coupon.exception.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author WuKB
 * @Date 2020/5/24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DistributionServiceTest {

    @Autowired
    private IUserService userService;

    private Long fakeUserId = 1009L;

    @Test
    public void testFindCouponByStatus() throws ServiceException {

        System.out.println(JSON.toJSONString(
                userService.findCouponsByStatus(
                        fakeUserId,
                        CouponStatus.USABLE.getCode()
                )
        ));
        System.out.println(JSON.toJSONString(
                userService.findCouponsByStatus(
                        fakeUserId,
                        CouponStatus.USED.getCode()
                )
        ));
        System.out.println(JSON.toJSONString(
                userService.findCouponsByStatus(
                        fakeUserId,
                        CouponStatus.EXPIRED.getCode()
                )
        ));
    }

    @Test
    public void testFindAvailableTemplate() throws ServiceException {

        System.out.println(JSON.toJSONString(
                userService.findAvailableTemplate(fakeUserId)
        ));
    }
}
