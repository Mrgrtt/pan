package com.haylen.pan.service.impl;

import com.haylen.pan.bo.OwnerDetails;
import com.haylen.pan.entity.Owner;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author haylen
 * @date 2019-12-26
 */
@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;

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
}
