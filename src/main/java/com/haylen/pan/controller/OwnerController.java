package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.dto.OwnerParam;
import com.haylen.pan.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author haylen
 * @date 2019-12-26
 */
@RestController
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult register(@Valid @RequestBody OwnerParam ownerParam) {
        if (ownerService.register(ownerParam) == null) {
            return CommonResult.failed("用户名已存在");
        }
        return CommonResult.success("", "注册成功");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@NotEmpty @RequestParam String username,
                              @NotEmpty @RequestParam String password) {
        String token = ownerService.login(username, password);
        if (token == null) {
            return CommonResult.failed("用户名或密码错误");
        }
        return CommonResult.success(token);
    }
}
