package com.github.lizhiwei88.dependencies.common.exception;


import com.github.lizhiwei88.dependencies.common.ResultCodeEnum;

/**
 * @author lizhiwei
 */
public class ForbiddenException extends DependenciesException {

    public ForbiddenException(String message) {
        super(ResultCodeEnum.FORBIDDEN, message);
    }

    public ForbiddenException() {
        super(ResultCodeEnum.FORBIDDEN);
    }
}
