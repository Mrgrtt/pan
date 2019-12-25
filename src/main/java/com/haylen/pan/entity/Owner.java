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
@Table(name = "owner")
public class Owner {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "bigint unsigned")
    private Long id;
    @Column(name = "username", columnDefinition = "varchar(64)", nullable = false)
    private String username;
    @Column(name = "password", columnDefinition = "char(64)", nullable = false)
    private String password;
    @Column(name = "gmt_create", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtCreate;
    @Column(name = "gmt_modified", columnDefinition = "datetime", nullable = false)
    private LocalDateTime gmtModified;
}
