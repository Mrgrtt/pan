package com.haylen.pan.exception;

/**
 * @author Haylen
 * @date 2020-03-20
 */
public class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
    }
}
