package com.github.lizhiwei88.dependencies.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Zhiwei Li
 */
@Data
@Builder
public class NodeVO {

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点分类
     */
    private Integer category;

    /**
     * 节点信息
     */
    private Info info;

    @Data
    @Builder
    public static class Info {

        /**
         * 节点类型 服务/接口
         */
        private String type;

        /**
         * 接口方法
         */
        private String method;

        /**
         * 依赖数量
         */
        private Integer dependencies;
    }
}
