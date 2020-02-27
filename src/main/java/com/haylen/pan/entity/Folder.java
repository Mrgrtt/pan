package com.haylen.pan.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 文件夹
 * @author haylen
 * @date 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "folder")
public class Folder extends BaseEntity{
    @Column(name = "owner_id", columnDefinition = "bigint unsigned", nullable = false)
    private Long ownerId;

    /**
     * 父文件夹Id 默认为0，表示位于根目录
     */
    @Column(name = "parent_id", columnDefinition = "bigint unsigned default 0", nullable = false)
    private Long parentId;

    @Column(name = "name", columnDefinition = "varchar(64)", nullable = false)
    private String name;
}
