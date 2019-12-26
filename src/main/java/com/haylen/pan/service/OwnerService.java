package com.haylen.pan.service;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.entity.Owner;

/**
 * @author haylen
 * @date 2019-12-26
 */
public interface OwnerService {
    /**
     * 根据用户名获取用户
     * @param name 用户名
     * @return 用户
     */
    Owner getOwnerByUsername(String name);

    /**
     * 根据用户名获取用户细节
     * @param name 用户名
     * @return 用户细节
     */
    OwnerDetails getOwnerDetailsByUsername(String name);
}
