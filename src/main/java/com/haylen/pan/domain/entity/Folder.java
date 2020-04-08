package com.haylen.pan.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 文件夹
 * @author haylen
 * @date 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "folder")
public class Folder extends BaseEntity {

    @JsonIgnore
    @Column(name = "owner_id", columnDefinition = "bigint unsigned", nullable = false)
    private Long ownerId;

    /**
     * 父文件夹Id 默认为0，表示位于根目录
     */
    @Column(name = "parent_id", columnDefinition = "bigint unsigned default 0", nullable = false)
    private Long parentId;

    @Column(name = "name", columnDefinition = "varchar(64)", nullable = false)
    private String name;

    /**
     * 状态，0->正常，1->可回收（回收站中显示），2->已删除，
     * 3->可回收（回收站中不显示，将文件夹放入回收站，其子孙文件夹处于该状态）
     */
    @JsonIgnore
    @Column(columnDefinition = "tinyint unsigned default 0", nullable = false)
    private Integer status;
}
