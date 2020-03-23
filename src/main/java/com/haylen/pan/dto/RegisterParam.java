package com.haylen.pan.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author haylen
 * @date 2019-12-26
 */
@Data
public class RegisterParam {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @Length(message = "密码最短为8位", min = 8)
    private String password;
}
