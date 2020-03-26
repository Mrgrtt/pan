package com.haylen.pan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author haylen
 * @date 2020-03-06
 */
@Data
public class OwnerInfoResult {

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("头像URL")
    private String avatar;
}
