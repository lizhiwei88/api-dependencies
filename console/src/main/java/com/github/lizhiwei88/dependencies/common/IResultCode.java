package com.github.lizhiwei88.dependencies.common;

/**
 * @author Zhiwei Li
 */
public interface IResultCode {

    /**
     * 获取code
     *
     * @return code
     */
    String getCode();

    /**
     * 获取message
     *
     * @return message
     */
    String getMessage();

    /**
     * 获取命名空间
     *
     * @return 错误类型
     */
    default String getNamespace() {
        return "";
    }

    /**
     * 返回命名空间+Code
     *
     * @return 错误码
     */
    default String getErrorCode() {
        return getNamespace() + getCode();
    }
}
