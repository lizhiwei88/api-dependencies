package com.github.lizhiwei88.dependencies.common;

import java.io.Serializable;

/**
 * @author lizhiwei
 */
public class Result<T> implements Serializable {

    /**
     * 响应数据Code
     */
    private String code;

    /**
     * 数据体
     */
    private T data;

    /**
     * 提示信息
     */
    private String message;

    public Result() {
    }

    public Result(IResultCode resultCode) {
        this(resultCode.getErrorCode(), resultCode.getMessage(), null);
    }

    public Result(IResultCode resultCode, String message) {
        this(resultCode.getErrorCode(), message, null);
    }

    public Result(IResultCode resultCode, T data) {
        this(resultCode.getErrorCode(), resultCode.getMessage(), data);
    }

    public Result(String code, String message) {
        this(code, message, null);
    }

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCodeEnum.SUCCESS, data);
    }

    public static Result<Void> success() {
        return new Result<>(ResultCodeEnum.SUCCESS);
    }

    public static Result<Void> error(String message) {
        return new Result<>(ResultCodeEnum.ERROR, message);
    }

    public static Result<Void> build(IResultCode resultCode) {
        return new Result<>(resultCode);
    }

    public static Result<Void> build(IResultCode resultCode, String message) {
        return new Result<>(resultCode, message);
    }

    public static Result<Void> build(String code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> build(String code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
