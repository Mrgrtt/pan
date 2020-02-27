package com.haylen.pan.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author haylen
 * @date 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owner")
public class Owner extends BaseEntity{
    @Column(name = "username", columnDefinition = "varchar(64)", nullable = false, unique = true)
    private String username;

    /**
     * 加密后的密码
     */
    @Column(name = "password", columnDefinition = "char(64)", nullable = false)
    private String password;
}
