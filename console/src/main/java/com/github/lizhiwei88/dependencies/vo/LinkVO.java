package com.github.lizhiwei88.dependencies.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Zhiwei Li
 */
@Data
@Builder
public class LinkVO {

    /**
     * 源节点
     */
    private String source;

    /**
     * 目标节点
     */
    private String target;
}
