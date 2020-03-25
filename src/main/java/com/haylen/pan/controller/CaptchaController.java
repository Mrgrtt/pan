package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import java.io.IOException;

/**
 * @author haylen
 * @date 2020-03-23
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @RequestMapping("/image/{token}")
    public void imageBuild(@PathVariable String token, HttpServletResponse response) {
        String imageType = "image/jpeg";
        response.setContentType(imageType);
        response.addHeader("Cache-Control", "no-cache");
        try {
            captchaService.imageBuild(response.getOutputStream(),token);
        } catch (IOException e) {
            throw new ApiException("服务器错误");
        }
    }

    @RequestMapping(value = "/email/{token}", method = RequestMethod.POST)
    public CommonResult emailCaptcha(@PathVariable String token,
                                     @RequestParam @Email String email) {
        captchaService.sendEmailCaptcha(email, token);
        return CommonResult.success();
    }

    @RequestMapping("/verify/{token}")
    public CommonResult verify(@PathVariable String token, @RequestParam String captcha) {
        return CommonResult.success(captchaService.verify(token, captcha));
    }
}
