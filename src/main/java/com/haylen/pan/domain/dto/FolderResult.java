package com.haylen.pan.domain.dto;

import com.haylen.pan.domain.entity.Folder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author haylen
 * @date 2020-5-27
 */
@Data
public class FolderResult {
    @ApiModelProperty("文件id")
    private Long id;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime modifiedTime;

    @ApiModelProperty("父文件夹id")
    private Long parentId;

    @ApiModelProperty("文件夹id")
    private String name;

    public static FolderResult valueOf(Folder folder) {
        FolderResult folderResult = new FolderResult();
        folderResult.setId(folder.getId());
        folderResult.setCreateTime(folder.getGmtCreate());
        folderResult.setModifiedTime(folder.getGmtModified());
        folderResult.setParentId(folder.getParentId());
        folderResult.setName(folder.getName());
        return folderResult;
    }
}
