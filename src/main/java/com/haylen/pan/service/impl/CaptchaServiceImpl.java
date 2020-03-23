package com.haylen.pan.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.service.CacheService;
import com.haylen.pan.service.CaptchaService;
import com.haylen.pan.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author haylen
 * @date 2020-03-23
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private CacheService cacheService;

    /** 验证码过期时间 */
    private static final long EXPIRED_TIME_MS = 2 * 60 * 1000;
    private static final String CACHE_KEY_PREFIX = "captcha";

    @Override
    public void imageBuild(OutputStream os, String token) {
        int high = 60;
        int width = 200;
        int length = 4;
        String captcha = getCaptcha(length);
        try {
            CaptchaUtil.createAuthCodeImage(captcha, high, width, os);
        } catch (IOException e) {
            throw new ApiException("验证码生成失败");
        }
        cacheService.setObject(CACHE_KEY_PREFIX + token, captcha, EXPIRED_TIME_MS);
    }

    @Override
    public void sendEmailCaptcha(String email, String token) {
    }

    @Override
    public Boolean verify(String token, String captcha) {
        String key = CACHE_KEY_PREFIX + token;
        String realCaptcha = cacheService.getObject(key);
        if (realCaptcha == null) {
            return false;
        }
        if (realCaptcha.toLowerCase().equals(captcha.toLowerCase())) {
            return true;
        }
        if (!cacheService.delObject(key)) {
            throw new ApiException("服务器错误");
        }
        return false;
    }

    private String getCaptcha(int length) {
        StringBuilder captchaBuilder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            captchaBuilder.append(RandomUtil.randomChar());
        }
        return captchaBuilder.toString();
    }
}
