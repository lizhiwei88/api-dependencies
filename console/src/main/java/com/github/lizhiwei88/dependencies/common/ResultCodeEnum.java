package com.github.lizhiwei88.dependencies.common;

/**
 * 基础异常code
 *
 * @author lizhiwei
 */
public enum ResultCodeEnum implements IResultCode {

    /**
     * 请求成功
     */
    SUCCESS("00000", "请求成功"),

    /**
     * 请求错误
     */
    ERROR("40000", "请求错误"),

    /**
     * 用户未认证
     */
    UNAUTHORIZED("41000", "用户未认证"),

    /**
     * 权限不足
     */
    FORBIDDEN("42000", "权限不足"),

    /**
     * 参数校验错误
     */
    VALIDATION("60000", "参数校验错误"),

    ;

    private final String code;

    private final String message;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
