package com.haylen.pan.repository;

import com.haylen.pan.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author haylen
 * @date 2019-12-25
 */
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户
     */
    Optional<Owner> findByUsername(String username);

    /**
     * 修改密码
     * @param id 用户id
     * @param newPassword 新密码
     * @param time 时间
     * @return 结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Owner o set o.password = ?1, o.gmtModified = ?2 where o.id = ?3")
    int updatePassword(String newPassword, LocalDateTime time, Long id);
}
