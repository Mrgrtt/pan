package com.haylen.pan.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author haylen
 * @date 2019-12-25
 */
@Data
@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "bigint unsigned")
    private Long id;
    @Column(name = "owner_id", columnDefinition = "bigint unsigned", nullable = false)
    private Long ownerId;
    /* 父目录Id */
    @Column(name = "parent_id", columnDefinition = "bigint unsigned")
    private Long parentId;
    @Column(name = "name", columnDefinition = "varchar(64)", nullable = false)
    private String name;
    @Column(name = "gmt_create", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtCreate;
    @Column(name = "gmt_modified", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtModified;
}
