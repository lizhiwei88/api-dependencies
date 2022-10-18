package com.github.lizhiwei88.dependencies.service;

import com.github.lizhiwei88.dependencies.dto.DependenciesInfoDTO;
import com.github.lizhiwei88.dependencies.service.bo.DependenciesInfoBO;

import java.util.List;
import java.util.Map;

/**
 * @author Zhiwei Li
 */
public interface IDependenciesService {

    /**
     * 上报接口依赖信息
     *
     */
    void report(DependenciesInfoDTO dependenciesInfoDTO);

    /**
     * 获取接口依赖信息
     *
     */
    Map<String, List<DependenciesInfoBO>> getDependenciesMap();
}
