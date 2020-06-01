package com.wkb.coupon.service;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.constant.CouponCategory;
import com.wkb.coupon.constant.DistributeTarget;
import com.wkb.coupon.constant.PeriodType;
import com.wkb.coupon.constant.ProductLineE;
import com.wkb.coupon.vo.TempCreateReq;
import com.wkb.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * @author WuKB
 * @Date 2020/5/18
 */
@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class BuildServiceTest {
    @Autowired
    private IBuildTemplateService buildTemplateService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testSeesionCallBack() throws Exception{
        System.out.println(String.format("%s,进入test方法", new Date()));
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                System.out.println(String.format("%s,进入sessionCallback方法", new Date()));
                redisOperations.opsForValue().
                        set("name", "wkb");
                int result = 10/0;
                redisOperations.opsForValue().
                        set("sex", "man");
                return null;
            }
        };
        redisTemplate.executePipelined(sessionCallback);
        Thread.sleep(5000);
        System.out.println("end test");
    }

    /*@Test
    public void testBuildTemplate() throws Exception {

        System.out.println(JSON.toJSONString(
                buildTemplateService.buildTemplate(fakeTemplateRequest())
        ));
        Thread.sleep(5000);
    }*/

    /**
     * <h2>fake TemplateRequest</h2>
     * */
    private TempCreateReq fakeTemplateRequest() {

        TempCreateReq request = new TempCreateReq();
        request.setName("优惠券模板-" + new Date().getTime());
        request.setLogo("http://www.imooc.com");
        request.setDesc("这是一张优惠券模板");
        request.setCategory(CouponCategory.DIRECT_CUT_COUPON.getCode());
        request.setProductLine(ProductLineE.TAO_ZHUAN.getCode());
        request.setCount(10000);
        request.setUserId(10001L);  // fake user id
        request.setTarget(DistributeTarget.SINGLE.getCode());

        TemplateRule rule = new TemplateRule();
        rule.setExpiration(new TemplateRule.Expiration(
                PeriodType.SHIFT.getCode(),
                1, DateUtils.addDays(new Date(), 60).getTime()
        ));
        rule.setDiscount(new TemplateRule.Discount(5, 1));
        rule.setLimitation(1);
        rule.setUsage(new TemplateRule.Usage(
                "广东省", "深圳市",
                JSON.toJSONString(Arrays.asList("文娱", "家居"))
        ));
        rule.setWeight(JSON.toJSONString(Collections.EMPTY_LIST));

        request.setRule(rule);

        return request;
    }
}
