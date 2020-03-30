package com.haylen.pan.domain.dto;

/**
 * @author haylen
 * @date 2019-12-25
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    NOT_FOUND(404, "访问地址不存在"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    BAD_REQUEST(400, "参数解析失败"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    FORBIDDEN(403, "没有相关权限");
    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
