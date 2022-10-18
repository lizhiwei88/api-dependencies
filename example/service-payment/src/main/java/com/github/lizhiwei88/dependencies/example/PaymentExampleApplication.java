package com.github.lizhiwei88.dependencies.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Zhiwei Li
 */
@SpringBootApplication
@EnableFeignClients({"com.github.lizhiwei88.dependencies.example.client"})
public class PaymentExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentExampleApplication.class, args);
    }
}
