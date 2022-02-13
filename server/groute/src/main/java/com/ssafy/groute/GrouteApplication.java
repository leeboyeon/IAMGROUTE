package com.ssafy.groute;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "com.ssafy.groute.mapper")
public class GrouteApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrouteApplication.class, args);
	}

}
