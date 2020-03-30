package com.haylen.pan.controller;

import com.haylen.pan.domain.dto.CommonResult;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "CaptchaController", value = "验证码管理")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @ApiOperation("获取图片验证码")
    @RequestMapping(value = "/image/{token}", method = RequestMethod.GET)
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

    @ApiOperation("发送邮箱验证码")
    @RequestMapping(value = "/email/{token}", method = RequestMethod.POST)
    public CommonResult emailCaptcha(@PathVariable String token,
                                     @RequestParam @Email String email) {
        captchaService.sendEmailCaptcha(email, token);
        return CommonResult.success();
    }

    @ApiOperation("验证验证码")
    @RequestMapping(value = "/verify/{token}", method = RequestMethod.GET)
    public CommonResult<Boolean> verify(@PathVariable String token, @RequestParam String captcha) {
        return CommonResult.success(captchaService.verify(token, captcha));
    }
}
