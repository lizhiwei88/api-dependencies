package com.github.lizhiwei88.dependencies.dto;

import lombok.Data;

/**
 * @author Zhiwei Li
 */
@Data
public class ApiDependenciesDTO {

    private String targetService;

    private String method;

    private String uri;
}
