package com.haylen.pan.service.impl;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.dto.OwnerParam;
import com.haylen.pan.entity.Owner;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.OwnerService;
import com.haylen.pan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public Owner getOwnerByUsername(String name) {
        return ownerRepository.findByUsername(name);
    }

    @Override
    public OwnerDetails getOwnerDetailsByUsername(String name) {
        Owner owner = ownerRepository.findByUsername(name);
        if (owner == null) {
            return null;
        }
        return new OwnerDetails(owner);
    }

    @Override
    public Owner register(OwnerParam ownerParam) {
        //用户名已存在
        if (ownerRepository.findByUsername(ownerParam.getUsername()) != null) {
            return null;
        }
        Owner owner = new Owner();
        owner.setUsername(ownerParam.getUsername());
        String encodedPassword = passwordEncoder.encode(ownerParam.getPassword());
        owner.setPassword(encodedPassword);
        owner.setGmtCreate(LocalDateTime.now());
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
}
