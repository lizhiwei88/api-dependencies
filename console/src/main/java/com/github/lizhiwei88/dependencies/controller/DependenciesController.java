package com.github.lizhiwei88.dependencies.controller;

import com.github.lizhiwei88.dependencies.dto.DependenciesInfoDTO;
import com.github.lizhiwei88.dependencies.service.IDependenciesService;
import com.github.lizhiwei88.dependencies.service.bo.DependenciesInfoBO;
import com.github.lizhiwei88.dependencies.vo.DependenciesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zhiwei Li
 */
@RestController
@RequestMapping("/api/dependencies")
public class DependenciesController {

    @Autowired
    private IDependenciesService dependenciesService;

    @GetMapping
    public List<DependenciesVO> dependencies() {
        Map<String, List<DependenciesInfoBO>> dependenciesMap = dependenciesService.getDependenciesMap();
        return dependenciesMap.entrySet()
                .stream().map(item -> DependenciesVO.builder()
                        .service(item.getKey())
                        .apiDependenciesList(item.getValue().stream()
                                .map(dependenciesInfoBO -> DependenciesVO.ApiDependenciesVO
                                        .builder()
                                        .method(dependenciesInfoBO.getMethod())
                                        .uri(dependenciesInfoBO.getUri())
                                        .targetService(dependenciesInfoBO.getTargetService())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping
    public void report(@RequestBody DependenciesInfoDTO dependenciesInfoDTO) {
        dependenciesService.report(dependenciesInfoDTO);
    }
}
