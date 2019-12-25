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
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "bigint unsigned")
    private Long id;
    @Column(name = "catalog_id", columnDefinition = "bigint unsigned")
    private Long catalogId;
    @Column(name = "owner_id", columnDefinition = "bigint unsigned", nullable = false)
    private Long ownerId;
    @Column(columnDefinition = "varchar(64)", nullable = false)
    private String name;
    @Column(name = "save_key", columnDefinition = "varchar(64)", nullable = false)
    private String saveKey;
    @Column(name = "media_type", columnDefinition = "varchar(64)")
    private String mediaType;
    @Column(name = "gmt_create", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtCreate;
    @Column(name = "gmt_modified", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtModified;
}
