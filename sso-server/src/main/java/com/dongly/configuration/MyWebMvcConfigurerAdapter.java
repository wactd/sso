package com.dongly.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by tiger on 2017/7/18.
 */

@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 防止html页面不能访问静态资源
        registry.addResourceHandler("/static/**") // 请求URL
                .addResourceLocations("classpath:static/"); // 本地路径
    }

}
