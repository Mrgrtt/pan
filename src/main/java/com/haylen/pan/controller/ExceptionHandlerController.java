package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author haylen
 * @date 2019-12-30
 */
@RestController
public class ExceptionHandlerController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping(value = "/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object error(HttpServletRequest request, HttpServletResponse response) {

        int status = response.getStatus();
        if (status == HttpStatus.NOT_FOUND.value()) {
            return CommonResult.notFound();
        }
        return CommonResult.failed();
    }
}
