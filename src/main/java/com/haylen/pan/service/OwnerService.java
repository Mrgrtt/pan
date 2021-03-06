package com.haylen.pan.service;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.domain.dto.LoginParam;
import com.haylen.pan.domain.dto.OwnerResult;
import com.haylen.pan.domain.dto.RegisterParam;
import com.haylen.pan.domain.dto.PasswordParam;
import com.haylen.pan.domain.entity.Owner;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务
 * @author haylen
 * @date 2019-12-26
 */
public interface OwnerService {

    /**
     * 根据用户名获取用户细节
     */
    OwnerDetails getOwnerDetailsByUsername(String name);

    /**
     * 注册
     */
    Owner register(RegisterParam registerParam);

    /**
     * 登录
     * @return token
     */
    String login(LoginParam loginParam);

    /**
     * 获取现在登录的用户
     */
    Owner getCurrentOwner();

    /**
     * 获取现在登录的用户id
     */
    Long getCurrentOwnerId();

    /**
     * 改密码
     */
    int updatePassword(PasswordParam passwordParam);

    /**
     * 该用户名是否已被注册
     */
    Boolean isRegistered(String name);
}
