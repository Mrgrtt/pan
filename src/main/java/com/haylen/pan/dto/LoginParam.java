package com.haylen.pan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author haylen
 * @date 2020-03-25
 */
@Data
public class LoginParam {
    @ApiModelProperty("用户名")
    @NotEmpty
    private String username;

    @ApiModelProperty("密码")
    @Length(min = 8)
    private String password;

    @ApiModelProperty("获取验证码时传的token")
    @NotEmpty
    private String token;

    @ApiModelProperty("验证码")
    @NotEmpty
    private String captcha;
}
