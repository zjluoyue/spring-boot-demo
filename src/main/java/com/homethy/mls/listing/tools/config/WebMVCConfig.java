package com.homethy.mls.listing.tools.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jia on 17-7-6.
 * springMVC的配置类
 */
@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    // 静态文件的路径添加
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
    }

}