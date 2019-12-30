package com.haylen.pan.component;

import com.haylen.pan.dto.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * 全局统一异常处理
 * @author haylen
 * @date 2019-12-30
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, BindException.class,
            ServletRequestBindingException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public CommonResult handleHttpMessageNotReadableException(Exception e) {
        log.info("参数解析失败", e);
        if (e instanceof BindException){
            return CommonResult.validateFailed(((BindException)e)
                    .getBindingResult().getFieldError().getDefaultMessage());
        }
        return CommonResult.validateFailed();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("不支持当前请求方法", e);
        return CommonResult.methodNotAllowed();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(DataAccessException.class)
    public CommonResult handleDataAccessException(DataAccessException e) {
        log.info("数据库访数据访问异常", e);
        return CommonResult.validateFailed();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public CommonResult handleException(Throwable e) {
        log.error("服务运行异常", e);
        return CommonResult.failed();
    }
}
