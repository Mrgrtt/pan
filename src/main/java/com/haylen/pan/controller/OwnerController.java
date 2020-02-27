package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.dto.OwnerParam;
import com.haylen.pan.dto.PasswordParam;
import com.haylen.pan.entity.Owner;
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
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestParam @NotEmpty String username,
                              @RequestParam @NotEmpty String password) {
        String token = ownerService.login(username, password);
        if (token == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(token);
    }

    @RequestMapping(value = "/update/password", method = RequestMethod.POST)
    public CommonResult changePassword(@Valid @RequestBody PasswordParam passwordParam) {
        if (ownerService.updatePassword(passwordParam) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @RequestMapping("/isRegistered")
    public CommonResult isRegistered(@RequestParam @NotEmpty String username) {
        return CommonResult.success(ownerService.isRegistered(username));
    }
}
