package com.haylen.pan.domain.dto;

import lombok.Data;

/**
 * @author haylen
 * @date 2019-12-25
 */
@Data
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    private CommonResult() {
    }

    private CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     */
    public static CommonResult<String> success() {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), "");
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static CommonResult<String> failed(ResultCode errorCode) {
        return new CommonResult<>(errorCode.getCode(), errorCode.getMessage(), "");
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static CommonResult<String> failed(String message) {
        return new CommonResult<>(ResultCode.FAILED.getCode(), message, "");
    }

    /**
     * 失败返回结果
     */
    public static CommonResult<String> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static CommonResult<String> validateFailed() {
        return validateFailed(ResultCode.BAD_REQUEST.getMessage());
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static CommonResult<String> validateFailed(String message) {
        return new CommonResult<>(ResultCode.BAD_REQUEST.getCode(), message, "");
    }

    /**
     * 404
     * @param message 提示信息
     */
    public static CommonResult<String> notFound(String message) {
        return new CommonResult<>(ResultCode.NOT_FOUND.getCode(), message, "");
    }

    /**
     * 404
     */
    public static CommonResult<String> notFound() {
        return notFound(ResultCode.NOT_FOUND.getMessage());
    }

    /**
     * 405
     */
    public static CommonResult<String> methodNotAllowed() {
        return new CommonResult<>(ResultCode.METHOD_NOT_ALLOWED.getCode(),
                ResultCode.METHOD_NOT_ALLOWED.getMessage(), "");
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }
}
