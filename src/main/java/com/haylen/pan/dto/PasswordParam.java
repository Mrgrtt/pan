package com.haylen.pan.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author haylen
 * @date 2019-12-29
 */
@Data
public class PasswordParam {
    @Length(min = 8)
    private String newPassword;
    @Length(min = 8)
    private String oldPassword;
}
