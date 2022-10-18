package com.github.lizhiwei88.dependencies.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Zhiwei Li
 */
@FeignClient(name = "example-service-user", path = "/api", url = "http://service-user")
public interface UserClient {

    @DeleteMapping("/user")
    void delInfo(@RequestParam(value = "id") String id);

    @PostMapping("/user")
    void addInfo();

    @PutMapping("/user/{id}")
    void updateInfo(@PathVariable(value = "id") String id);
}
