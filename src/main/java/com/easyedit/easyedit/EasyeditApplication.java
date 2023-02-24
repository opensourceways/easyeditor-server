package com.easyedit.easyedit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.easyedit.*"})
public class EasyeditApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyeditApplication.class, args);
	}

}
