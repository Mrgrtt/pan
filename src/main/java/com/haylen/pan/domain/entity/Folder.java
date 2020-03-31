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
public class Folder extends BaseEntity{

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
     * 删除标志， 1->已删除 0->未删除
     */
    @JsonIgnore
    @Column(columnDefinition = "tinyint unsigned default 0", nullable = false)
    private Integer deleted;
}
