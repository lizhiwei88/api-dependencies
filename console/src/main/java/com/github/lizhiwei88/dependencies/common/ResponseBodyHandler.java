package com.github.lizhiwei88.dependencies.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lizhiwei88.dependencies.common.exception.DependenciesException;
import com.github.lizhiwei88.dependencies.common.exception.ForbiddenException;
import com.github.lizhiwei88.dependencies.common.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 全局统一返回拦截器
 *
 * @author Zhiwei Li
 */
@RestControllerAdvice
@Slf4j
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {

    private ObjectMapper objectMapper;

    public ResponseBodyHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result) {
            return body;
        }
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(Result.success(body));
            } catch (JsonProcessingException e) {
                throw new ClassCastException(e.getMessage());
            }
        }
        return Result.success(body);
    }

    /**
     * 业务错误
     *
     * @return Result
     */
    @ExceptionHandler(DependenciesException.class)
    @ResponseBody
    public Result<Void> baseException(DependenciesException exception) {
        return Result.build(exception.getCode(), exception.getMessage());
    }

    /**
     * 未知错误
     *
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> exception(Exception exception) {
        log.error("INTERNAL SERVER ERROR.", exception);
        String message = StringUtils.hasText(exception.getMessage()) ? exception.getMessage() : exception.getClass().getSimpleName();
        return Result.error(message);
    }

    /**
     * 未授权
     *
     * @param exception
     * @return Result
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> unauthorizedException(UnauthorizedException exception) {
        return Result.build(exception.getCode(), exception.getMessage());
    }

    /**
     * 权限不足
     *
     * @return Result
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> forbiddenException(ForbiddenException exception) {
        return Result.build(exception.getCode(), exception.getMessage());
    }

    /**
     * Valid 校验失败
     *
     * @param exception
     * @return Result
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseBody
    public Result<Void> preconditionFailedException(Exception exception) {
        String message;
        if (exception instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            if (bindingResult.hasErrors()) {
                message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            } else {
                return Result.build(ResultCodeEnum.ERROR);
            }
        } else if (exception instanceof ConstraintViolationException) {
            message = ((ConstraintViolationException) exception).getConstraintViolations().stream()
                    .findFirst()
                    .map(ConstraintViolation::getMessageTemplate)
                    .orElse(null);
        } else {
            return Result.build(ResultCodeEnum.ERROR);
        }
        return Result.build(ResultCodeEnum.VALIDATION, message);
    }
}
