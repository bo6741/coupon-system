package com.wkb.coupon.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author WuKB
 * @Date 2020/5/14
 * 自定义http 消息转换器
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {

        converters.clear();
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
