package com.example.datasetManagerService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableFeignClients
@SpringBootApplication
public class DatasetManagerServiceTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatasetManagerServiceTestApplication.class, args);
    }
}
