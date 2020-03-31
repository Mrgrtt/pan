package com.haylen.pan.controller;

import com.haylen.pan.domain.dto.*;
import com.haylen.pan.domain.entity.Owner;
import com.haylen.pan.service.OwnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author haylen
 * @date 2019-12-26
 */
@RestController
@RequestMapping("/owner")
@Api(tags = "OwnerController", value = "用户管理")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @ApiOperation("注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult register(@Valid @RequestBody RegisterParam registerParam) {
        if (ownerService.register(registerParam) == null) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult<String> login(@Valid LoginParam loginParam) {
        String token = ownerService.login(loginParam);
        if (token == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(token);
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/update/password", method = RequestMethod.POST)
    public CommonResult changePassword(@Valid @RequestBody PasswordParam passwordParam) {
        if (ownerService.updatePassword(passwordParam) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("指定用户名是否已被注册")
    @RequestMapping(value = "/isRegistered", method = RequestMethod.GET)
    public CommonResult<Boolean> isRegistered(@RequestParam @NotEmpty String username) {
        return CommonResult.success(ownerService.isRegistered(username));
    }

    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<OwnerInfoResult> getInfo() {
        Owner owner = ownerService.getCurrentOwner();
        return CommonResult.success(OwnerInfoResult.valueOf(owner));
    }

    @ApiOperation("上传头像")
    @RequestMapping(value = "/avatar/upload", method = RequestMethod.POST)
    public CommonResult uploadAvatar(@RequestPart MultipartFile file) {
        if (ownerService.uploadAvatar(file) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }
}
