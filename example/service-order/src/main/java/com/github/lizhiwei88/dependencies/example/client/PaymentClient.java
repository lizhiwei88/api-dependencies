package com.github.lizhiwei88.dependencies.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author Zhiwei Li
 */
@FeignClient(name = "example-service-payment", path = "/api", url = "http://service-payment")
public interface PaymentClient {

    @GetMapping("/payment/{id}")
    void getInfo(@PathVariable(value = "id") String id);

    @PostMapping("/payment")
    void addInfo();

    @PutMapping("/payment/{id}")
    void updateInfo(@PathVariable(value = "id") String id);
}
