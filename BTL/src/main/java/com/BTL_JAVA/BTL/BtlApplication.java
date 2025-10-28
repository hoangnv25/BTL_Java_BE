package com.BTL_JAVA.BTL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtlApplication.class, args);
	}

}
