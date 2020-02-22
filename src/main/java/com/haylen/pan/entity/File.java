package com.haylen.pan.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 文件
 * @author haylen
 * @date 2019-12-25
 */
@Data
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
    @Column(name = "storage_key", columnDefinition = "varchar(64)", nullable = false, unique = true)
    private String storageKey;

    /**
     * 文件类型
     */
    @Column(name = "media_type", columnDefinition = "varchar(128) default ''", nullable = false)
    private String mediaType;

    /**
     * 文件大小
     */
    @Column(columnDefinition = "int unsigned", nullable = false)
    private Long size;
}
