package com.hasikiFire.networkmall;

import java.util.TimeZone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

@MapperScan("com.hasikiFire.networkmall.dao.mapper")
@MapperScan("com.hasikiFire.networkmall.dao.core")
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class NetworkMallApplication {

	public static void main(String[] args) {
		// 设置默认时区
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SpringApplication.run(NetworkMallApplication.class, args);
	}

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		PaginationInnerInterceptor pagination = new PaginationInnerInterceptor(DbType.MYSQL);
		pagination.setMaxLimit(1000L); // 可选：设置单页最大记录数
		interceptor.addInnerInterceptor(pagination);
		return interceptor;
	}
}