package com.github.lizhiwei88.dependencies.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Zhiwei Li
 */
@Data
public class DependenciesInfoDTO {

    private String service;

    private List<ApiDependenciesDTO> apiDependenciesList;
}
