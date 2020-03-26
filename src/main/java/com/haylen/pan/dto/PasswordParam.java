package com.haylen.pan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author haylen
 * @date 2019-12-29
 */
@Data
public class PasswordParam {

    @ApiModelProperty("新密码")
    @Length(min = 8, message = "密码最短为8位")
    private String newPassword;

    @ApiModelProperty("旧密码")
    @Length(min = 8, message = "密码最短为8位")
    private String oldPassword;
}
