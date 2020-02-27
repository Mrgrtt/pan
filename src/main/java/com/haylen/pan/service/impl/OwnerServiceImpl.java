package com.haylen.pan.service.impl;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.dto.OwnerParam;
import com.haylen.pan.dto.PasswordParam;
import com.haylen.pan.entity.Owner;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.OwnerService;
import com.haylen.pan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return ownerRepository.updatePassword(passwordEncoder.encode(passwordParam.getNewPassword()),
                LocalDateTime.now(), getCurrentOwnerId());
    }

    @Override
    public Boolean isRegistered(String name) {
        Optional<Owner> ownerOptional = ownerRepository.findByUsername(name);
        return ownerOptional.isPresent();
    }
}
