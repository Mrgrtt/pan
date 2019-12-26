package com.haylen.pan.repository;

import com.haylen.pan.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

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
    Owner findByUsername(String username);
}
