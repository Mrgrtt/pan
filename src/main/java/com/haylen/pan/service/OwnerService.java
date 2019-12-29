package com.haylen.pan.service;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.dto.OwnerParam;
import com.haylen.pan.dto.PasswordParam;
import com.haylen.pan.entity.Owner;

/**
 * 用户服务
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
    /**
     * 注册用户
     * @param ownerParam 注册参数
     * @return 用户
     */
    Owner register(OwnerParam ownerParam);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return Token
     */
    String login(String username, String password);

    /**
     * 获取现在登录的用户id
     * @return 现在登录的用户id
     */
    Long getCurrentOwnerId();

    /**
     * 改密码
     * @param passwordParam 密码参数
     * @return 结果
     */
    int updatePassword(PasswordParam passwordParam);
}
