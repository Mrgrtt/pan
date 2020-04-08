package com.haylen.pan.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 文件
 * @author haylen
 * @date 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "file")
public class File extends BaseEntity {
    /**
     * 文件夹Id 默认为0，表示位于根目录
     */
    @Column(name = "folder_id", columnDefinition = "bigint unsigned default 0")
    private Long folderId;

    @JsonIgnore
    @Column(name = "owner_id", columnDefinition = "bigint unsigned", nullable = false)
    private Long ownerId;

    @Column(columnDefinition = "varchar(64)", nullable = false)
    private String name;

    /**
     * 用于储存和检索文件
     */
    @Column(name = "storage_key", columnDefinition = "varchar(256)", nullable = false)
    private String storageKey;

    /**
     * 文件类型
     */
    @Column(name = "media_type", columnDefinition = "varchar(128) default ''", nullable = false)
    private String mediaType;

    /**
     * 文件大小(Byte)
     */
    @Column(columnDefinition = "int unsigned", nullable = false)
    private Long size;

    /**
     * 状态，0->正常，1->可回收（回收站中显示），2->已删除，
     * 3->可回收（回收站中不显示，将文件夹放入回收站，其子孙文件处于该状态）
     */
    @JsonIgnore
    @Column(columnDefinition = "tinyint unsigned default 0", nullable = false)
    private Integer status;
}
