package com.haylen.pan.domain.dto;

import com.haylen.pan.domain.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author haylen
 * @date 2020-5-26
 */
@Data
public class FileResult {
    @ApiModelProperty("文件id")
    private Long id;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime modifiedTime;

    @ApiModelProperty("文件夹id")
    private Long folderId;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("储存key")
    private String storageKey;

    @ApiModelProperty("文件大小（Byte）")
    private Long size;

    public static FileResult valueOf(File file) {
        FileResult fileResult = new FileResult();
        fileResult.setId(file.getId());
        fileResult.setCreateTime(file.getGmtCreate());
        fileResult.setFolderId(file.getFolderId());
        fileResult.setName(file.getName());
        fileResult.setStorageKey(file.getStorageKey());
        fileResult.setSize(file.getSize());
        return fileResult;
    }
}
