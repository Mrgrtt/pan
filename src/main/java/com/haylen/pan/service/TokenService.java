package com.haylen.pan.service;

/**
 * @author haylen
 * @date 2020-5-25
 */
public interface TokenService {
    /**
     * 构造token
     * @param subject token携带的内容
     * @return token
     */
    String builtToken(String subject);

    /**
     * 解析token携带的内容
     * @param token token
     * @exception TokenParserException 解析异常
     * @return 内容
     */
    String getSubjectByToken(String token) throws TokenParserException;

    final class TokenParserException extends Exception {
        public TokenParserException(String msg) {
            super(msg);
        }
    }
}
