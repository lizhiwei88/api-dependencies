package com.github.lizhiwei88.dependencies.common.exception;


import com.github.lizhiwei88.dependencies.common.ResultCodeEnum;

/**
 * @author lizhiwei
 */
public class UnauthorizedException extends DependenciesException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super(ResultCodeEnum.UNAUTHORIZED);
    }

}
