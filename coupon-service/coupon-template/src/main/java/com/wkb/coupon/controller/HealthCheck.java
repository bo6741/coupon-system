package com.wkb.coupon.controller;

import com.wkb.coupon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康检查接口
 * @author WuKB
 * @Date 2020/5/17
 */
@Slf4j
@RestController
public class HealthCheck {

    /** 服务发现客户端 */
    private final DiscoveryClient client;

    /** 服务注册接口, 提供了获取服务 id 的方法*/
    private final Registration registration;

    @Autowired
    public HealthCheck(DiscoveryClient client, Registration registration) {
        this.client = client;
        this.registration = registration;
    }

    /**
     * 健康检查接口
     * 127.0.0.1:7001/coupon-template/health
     * @return
     */
    @GetMapping("/health")
    public String health(){
        log.debug("view health api");
        return "couponTemplate is health";
    }

    /**
     * 异常检查接口
     * 127.0.0.1:7001/coupon-template/exception
     * @return
     * @throws ServiceException
     */
    @GetMapping("/exception")
    public String exception() throws ServiceException{
        log.debug("view exception api");
        throw new ServiceException("coupon-Template exception");
    }

    /**
     * 获取eureka server上的微服务元信息
     * 127.0.0.1:7001/coupon-template/exception
     * @return
     */
    @GetMapping("/info")
    public List<Map<String, Object>> info() {

        // 大约需要等待两分钟时间才能获取到注册信息
        List<ServiceInstance> instances =
                client.getInstances(registration.getServiceId());

        List<Map<String, Object>> result =
                new ArrayList<>(instances.size());

        instances.forEach(i -> {
            Map<String, Object> info = new HashMap<>();
            info.put("serviceId", i.getServiceId());
            info.put("instanceId", i.getInstanceId());
            info.put("port", i.getPort());

            result.add(info);
        });

        return result;
    }
}
