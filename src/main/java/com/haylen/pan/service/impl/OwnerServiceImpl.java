package com.haylen.pan.service.impl;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.dto.OwnerParam;
import com.haylen.pan.dto.PasswordParam;
import com.haylen.pan.entity.Owner;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.FileStorageService;
import com.haylen.pan.service.OwnerService;
import com.haylen.pan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    private final static String FILE_DOWNLOAD_URL = "/file/download/";

    @Override
    public Owner getOwnerByUsername(String name) {
        return ownerRepository.findByUsername(name).get();
    }

    @Override
    public OwnerDetails getOwnerDetailsByUsername(String name) {
        Optional<Owner> ownerOptional = ownerRepository.findByUsername(name);
        if (!ownerOptional.isPresent()) {
            return null;
        }
        return new OwnerDetails(ownerOptional.get());
    }

    @Override
    public Owner register(OwnerParam ownerParam) {
        if (isRegistered(ownerParam.getUsername())) {
            return null;
        }
        Owner owner = new Owner();
        owner.setUsername(ownerParam.getUsername());
        String encodedPassword = passwordEncoder.encode(ownerParam.getPassword());
        ownerParam = null;
        owner.setPassword(encodedPassword);
        owner.setGmtModified(LocalDateTime.now());
        return ownerRepository.save(owner);
    }

    @Override
    public String login(String username, String password) {
        Owner owner = getOwnerByUsername(username);
        if (owner == null) {
            return null;
        }
        if (!passwordEncoder.matches(password, owner.getPassword())) {
            return null;
        }
        return jwtUtil.builtToken(username);
    }

    @Override
    public Long getCurrentOwnerId() {
        return getCurrentOwner().getId();
    }

    @Override
    public Owner getCurrentOwner() {
        OwnerDetails details = (OwnerDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return details.getOwner();
    }

    @Override
    public int updatePassword(PasswordParam passwordParam) {
        String encodeOldPassword = getCurrentOwner().getPassword();
        if (!passwordEncoder.matches(passwordParam.getOldPassword(), encodeOldPassword)) {
            return 0;
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
        if (!file.getContentType().startsWith(imageContentTypePrefix)) {
            return 0;
        }
        String storageKey = fileStorageService.putFile(file);
        if (storageKey == null) {
            return 0;
        }
        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return 0;
        }
        String avatarUrl = "http://" + host + ":8080" + FILE_DOWNLOAD_URL + storageKey;
        return ownerRepository.updateAvatar(avatarUrl, getCurrentOwnerId());
    }
}
