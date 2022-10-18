package com.github.lizhiwei88.dependencies.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author Zhiwei Li
 */
@FeignClient(name = "example-service-order", path = "/api", url = "http://service-order")
public interface OrderClient {

    @GetMapping("/order/{id}")
    void getInfo(@PathVariable(value = "id") String id);

    @PostMapping("/order")
    void addInfo();

    @PutMapping("/order/{id}")
    void updateInfo(@PathVariable(value = "id") String id);
}
