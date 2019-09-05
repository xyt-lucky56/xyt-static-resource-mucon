package com.xyt.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;


@Configuration
@AutoConfigureBefore(MapperAutoConfiguration.class)
@PropertySource("classpath:tk.properties")
public class TkMapperConfiguration{
	/**
	 * TK很坑，初始化在配置中心拉取文件之前
	 */
}
