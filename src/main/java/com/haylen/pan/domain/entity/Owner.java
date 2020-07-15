package com.haylen.pan.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author haylen
 * @date 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owner")
public class Owner extends BaseEntity{
    /**
     * 用户命
     */
    @Column(name = "username", columnDefinition = "varchar(64)", nullable = false, unique = true)
    private String username;

    /**
     * 加密后的密码
     */
    @Column(name = "password", columnDefinition = "char(64)", nullable = false)
    private String password;

    /**
     * 总储存空间(byte)
     */
    @Column(name = "total_storage_space", columnDefinition = "bigint unsigned default 0", nullable = false)
    private Long totalStorageSpace;

    /**
     * 已用储存空间(byte)
     */
    @Column(name = "used_storage_space", columnDefinition = "bigint unsigned default 0", nullable = false)
    private Long usedStorageSpace;
}
