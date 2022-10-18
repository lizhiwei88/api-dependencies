package com.github.lizhiwei88.dependencies.service.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Zhiwei Li
 */
@Data
@Builder
public class DependenciesInfoBO {

    private String targetService;

    private String method;

    private String uri;
}
