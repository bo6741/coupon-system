package com.wkb.coupon.service;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.exception.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * 优惠券模板基础测试
 * @author WuKB
 * @Date 2020/5/18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TemplateBaseTest {

    @Autowired
    private ITemplateBaseService baseService;

    @Test
    public void testBuildTemplateInfo() throws ServiceException {

        System.out.println(JSON.toJSONString(
                baseService.buildTemplateInfo(10)));
        /*System.out.println(JSON.toJSONString(
                baseService.buildTemplateInfo( 2)));*/
    }

    @Test
    public void testFindAllUsableTemplate() {

        System.out.println(JSON.toJSONString(
                baseService.findAllUsableTemplate()
        ));
    }

    @Test
    public void testFindId2TemplateSDK() {

        System.out.println(JSON.toJSONString(
                baseService.findIds2TemplateSDK(Arrays.asList(1, 2, 3))
        ));
    }
}
