package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;
@SpringBootApplication
@MapperScan(basePackages="com.imooc.mapper")
@ComponentScan(basePackages= {"com.imooc","org.n3r.idworker"})
/**
 * 
 * @author 徐塬峰
 * 创建时间：2018年6月11日 下午4:00:56
 */
//http://localhost:8080/swagger-ui.html
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
