package com.anshuman.EC2.and.S3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class Ec2AndS3Application {

	public static void main(String[] args) {
		SpringApplication.run(Ec2AndS3Application.class, args);
	}

}
