package com.haylen.pan.service.impl;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.dto.LoginParam;
import com.haylen.pan.dto.RegisterParam;
import com.haylen.pan.dto.PasswordParam;
import com.haylen.pan.entity.Owner;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.CaptchaService;
import com.haylen.pan.service.FileStorageService;
import com.haylen.pan.service.OwnerService;
import com.haylen.pan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
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
    private JwtUtil jwtUtil;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private CaptchaService captchaService;

    private final static String FILE_DOWNLOAD_URL = "/file/download/";

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
        return jwtUtil.builtToken(loginParam.getUsername());
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
        return ownerRepository.updatePassword(
                passwordEncoder.encode(passwordParam.getNewPassword()),getCurrentOwnerId());
    }

    @Override
    public Boolean isRegistered(String name) {
        Optional<Owner> ownerOptional = ownerRepository.findByUsername(name);
        return ownerOptional.isPresent();
    }

    @Override
    public int uploadAvatar(MultipartFile file) {
        String imageContentTypePrefix = "image/";
        if (file.getContentType() == null
                || !file.getContentType().startsWith(imageContentTypePrefix)) {
            throw new ApiException("不被允许的文件类型");
        }
        String storageKey = fileStorageService.putFile(file);
        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new ApiException("服务器异常");
        }
        String avatarUrl = "http://" + host + ":8080" + FILE_DOWNLOAD_URL + storageKey;
        return ownerRepository.updateAvatar(avatarUrl, getCurrentOwnerId());
    }

    private Owner getOwnerByUsername(String name) {
        Optional<Owner> optionalOwner = ownerRepository.findByUsername(name);
        if (!optionalOwner.isPresent()) {
            throw new ApiException("用户不存在");
        }
        return optionalOwner.get();
    }
}
