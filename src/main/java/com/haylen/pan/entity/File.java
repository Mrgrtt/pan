package com.haylen.pan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

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
     * 删除标志， 1->已删除 0->未删除
     */
    @JsonIgnore
    @Column(columnDefinition = "tinyint unsigned default 0", nullable = false)
    private Integer deleted;
}
