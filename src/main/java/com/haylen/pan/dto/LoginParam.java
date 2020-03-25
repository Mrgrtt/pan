package com.haylen.pan.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author haylen
 * @date 2020-03-25
 */
@Data
public class LoginParam {
    @NotEmpty
    private String username;

    @Length(min = 8)
    private String password;

    @NotEmpty
    private String token;

    @NotEmpty
    private String captcha;
}
