package com.haylen.pan.repository;

import com.haylen.pan.domain.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
     * 更新密码
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Owner o set o.password = ?1, o.gmtModified = current_timestamp where o.id = ?2")
    int updatePassword(String newPassword, Long id);

    /**
     *获取已用存储空间
     */
    @Query("select o.usedStorageSpace from Owner o where o.id = ?1")
    Long getUsedStorageSpace(Long id);

    /**
     * 增加已用空间
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Owner o set o.usedStorageSpace = o.usedStorageSpace + ?1 where o.id = ?2")
    int increaseUsedStorageSpace(Long size, Long id);

    /**
     * 减少已用空间
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Owner o set o.usedStorageSpace = o.usedStorageSpace - ?1 where o.id = ?2")
    int reduceUsedStorageSpace(Long size, Long id);
}
