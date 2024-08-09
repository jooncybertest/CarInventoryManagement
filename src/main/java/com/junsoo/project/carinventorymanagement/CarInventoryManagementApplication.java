package com.junsoo.project.carinventorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CarInventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarInventoryManagementApplication.class, args);
    }

}
