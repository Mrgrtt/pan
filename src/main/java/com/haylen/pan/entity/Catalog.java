package com.haylen.pan.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 目录
 * @author haylen
 * @date 2019-12-25
 */
@Data
@Entity
@Table(name = "catalog")
public class Catalog extends BaseEntity{
    @Column(name = "owner_id", columnDefinition = "bigint unsigned", nullable = false)
    private Long ownerId;
    /**
     * 父目录Id
     */
    @Column(name = "parent_id", columnDefinition = "bigint unsigned default 0", nullable = false)
    private Long parentId;
    @Column(name = "name", columnDefinition = "varchar(64)", nullable = false)
    private String name;
}
