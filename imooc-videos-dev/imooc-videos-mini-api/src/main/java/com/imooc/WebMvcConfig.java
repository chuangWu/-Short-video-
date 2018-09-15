package com.imooc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//重写方法
		//修改tomcat 虚拟映射
		registry.addResourceHandler("/**").
		addResourceLocations("classpath:/META-INF/resources/").//启用动态发布
		addResourceLocations("file:C:/imooc_videos_dev/");//定义相对路径 很重要
		
		//发布到
	}

	
	
	
}
