package com.haylen.pan.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.domain.dto.LoginParam;
import com.haylen.pan.domain.dto.RegisterParam;
import com.haylen.pan.domain.dto.PasswordParam;
import com.haylen.pan.domain.entity.Owner;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author haylen
 * @date 2019-12-26
 */
@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private CacheService cacheService;
    @Value("${owner-total-space-default}")
    private Long totalSpaceDefault;
    private final static String OWNER_CACHE_KEY = "user";
    private final static long CACHE_TIME = 4 * 60 * 1000;

    @Override
    public OwnerDetails getOwnerDetailsByUsername(String name) {
        return new OwnerDetails(getOwnerByUsername(name));
    }

    @Override
    public Owner register(RegisterParam registerParam) {
        if (isRegistered(registerParam.getUsername())) {
            throw new ApiException("用户名已被注册");
        }
        Owner owner = new Owner();
        owner.setUsername(registerParam.getUsername());
        String encodedPassword = passwordEncoder.encode(registerParam.getPassword());
        owner.setPassword(encodedPassword);
        owner.setGmtModified(LocalDateTime.now());
        owner.setTotalStorageSpace(totalSpaceDefault);
        owner.setUsedStorageSpace(0L);
        return ownerRepository.save(owner);
    }

    @Override
    public String login(LoginParam loginParam) {
        if (!captchaService.verify(loginParam.getToken(), loginParam.getCaptcha())) {
            throw new ApiException("验证码错误");
        }
        Owner owner = getOwnerByUsername(loginParam.getUsername());
        if (!passwordEncoder.matches(loginParam.getPassword(), owner.getPassword())) {
            throw new ApiException("用户名或密码错误");
        }
        return tokenService.builtToken(loginParam.getUsername());
    }

    @Override
    public Long getCurrentOwnerId() {
        return getCurrentOwner().getId();
    }

    @Override
    public Owner getCurrentOwner() {
        OwnerDetails details = (OwnerDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Assert.notNull(details, "获取验证上下文失败");
        return details.getOwner();
    }

    @Override
    public int updatePassword(PasswordParam passwordParam) {
        String encodeOldPassword = getCurrentOwner().getPassword();
        if (!passwordEncoder.matches(passwordParam.getOldPassword(), encodeOldPassword)) {
            throw new ApiException("旧密码错误");
        }
        int r = ownerRepository.updatePassword(
                passwordEncoder.encode(passwordParam.getNewPassword()),getCurrentOwnerId());
        cacheService.delObject(OWNER_CACHE_KEY + getCurrentOwner().getUsername());
        return r;
    }

    @Override
    public Boolean isRegistered(String name) {
        Optional<Owner> ownerOptional = ownerRepository.findByUsername(name);
        return ownerOptional.isPresent();
    }

    private Owner getOwnerByUsername(String name) {
        Owner owner = (Owner) cacheService.getObject(OWNER_CACHE_KEY + name);
        if (owner != null) {
            return owner;
        }
        Optional<Owner> optionalOwner = ownerRepository.findByUsername(name);
        if (!optionalOwner.isPresent()) {
            throw new ApiException("用户不存在");
        }
        cacheService.setObject(OWNER_CACHE_KEY + name,
                optionalOwner.get(), CACHE_TIME + RandomUtil.randomLong(30 * 1000));
        return optionalOwner.get();
    }
}
