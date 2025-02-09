package com.asifiqbalsekh.EcomBE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class EcomBeApplication {

	public static void main(String[] args) {
		log.info("EcomBeApplication started");
		SpringApplication.run(EcomBeApplication.class, args);
	}

}
