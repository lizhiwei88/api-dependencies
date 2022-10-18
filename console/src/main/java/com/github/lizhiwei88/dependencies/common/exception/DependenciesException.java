package com.github.lizhiwei88.dependencies.common.exception;

import com.github.lizhiwei88.dependencies.common.IResultCode;
import com.github.lizhiwei88.dependencies.common.ResultCodeEnum;

/**
 * @author Zhiwei Li
 */
public class DependenciesException extends RuntimeException {

    private final String code;

    public DependenciesException(String message) {
        this(ResultCodeEnum.ERROR, message);
    }

    public DependenciesException(IResultCode resultCode) {
        this(resultCode, resultCode.getMessage());
    }

    public DependenciesException(IResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getErrorCode();
    }

    public String getCode() {
        return code;
    }
}
