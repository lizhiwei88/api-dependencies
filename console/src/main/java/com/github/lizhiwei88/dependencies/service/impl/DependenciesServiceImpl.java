package com.github.lizhiwei88.dependencies.service.impl;

import com.github.lizhiwei88.dependencies.dto.DependenciesInfoDTO;
import com.github.lizhiwei88.dependencies.service.IDependenciesService;
import com.github.lizhiwei88.dependencies.service.bo.DependenciesInfoBO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Zhiwei Li
 */
@Service
public class DependenciesServiceImpl implements IDependenciesService {

    private static final ConcurrentHashMap<String, List<DependenciesInfoBO>> DEPENDENCIES_MAP = new ConcurrentHashMap<>();

    @Override
    public void report(DependenciesInfoDTO dependenciesInfoDTO) {
        List<DependenciesInfoBO> list = dependenciesInfoDTO.getApiDependenciesList().stream()
                .map(apiDependenciesDTO -> DependenciesInfoBO.builder()
                        .method(apiDependenciesDTO.getMethod())
                        .targetService(apiDependenciesDTO.getTargetService())
                        .uri(apiDependenciesDTO.getUri())
                        .build())
                .collect(Collectors.toList());
        DEPENDENCIES_MAP.put(dependenciesInfoDTO.getService(), list);
    }

    @Override
    public Map<String, List<DependenciesInfoBO>> getDependenciesMap() {
        return DEPENDENCIES_MAP;
    }
}
