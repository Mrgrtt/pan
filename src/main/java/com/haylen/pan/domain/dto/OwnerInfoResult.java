package com.haylen.pan.domain.dto;

import com.haylen.pan.domain.entity.Owner;
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

    @ApiModelProperty("已使用存储空间(byte)")
    private Long usedStorageSpace;

    @ApiModelProperty("总储存空间(byte)")
    private Long totalStorageSpace;

    static public OwnerInfoResult valueOf(Owner owner) {
        OwnerInfoResult ownerInfoResult = new OwnerInfoResult();
        ownerInfoResult.setName(owner.getUsername());
        ownerInfoResult.setAvatar(owner.getAvatar());
        ownerInfoResult.setUsedStorageSpace(owner.getUsedStorageSpace());
        ownerInfoResult.setTotalStorageSpace(owner.getTotalStorageSpace());
        return ownerInfoResult;
    }
}
