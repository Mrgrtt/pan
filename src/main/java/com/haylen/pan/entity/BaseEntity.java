package com.haylen.pan.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Haylen
 * @date 2020-02-18
 */
@Data
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "bigint unsigned")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtCreate;

    /**
     * 最后编辑时间
     */
    @Column(name = "gmt_modified", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtModified;

    @PrePersist
    public void saveCreateTime() {
        if (gmtCreate == null) {
            gmtCreate = LocalDateTime.now();
        }
    }
}
