package com.haylen.pan.service;

import java.io.OutputStream;

/**
 * 验证码服务
 * @author haylen
 * @date 2020-03-23
 */
public interface CaptchaService {

    /**
     * 图片验证码生成
     * @param token 前端生成的唯一标记
     */
    void imageBuild(OutputStream os, String token);

    /**
     * 邮件验证码发送
     * @param token 前端生成的唯一标记
     */
    void sendEmailCaptcha(String email, String token);

    /**
     * 验证验证码
     * @param token 前端生成的唯一标记
     */
    Boolean verify(String token, String captcha);
}
