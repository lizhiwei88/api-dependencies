package com.github.lizhiwei88.dependencies.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Zhiwei Li
 */
@Data
@Builder
public class DependenciesVO {

    /**
     * 节点
     */
    private String service;

    /**
     * 依赖接口
     */
    private List<ApiDependenciesVO> apiDependenciesList;

    @Data
    @Builder
    public static class ApiDependenciesVO {

        private String targetService;

        private String method;

        private String uri;
    }
}
