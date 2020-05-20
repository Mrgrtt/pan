package com.haylen.pan.service.impl;

import com.haylen.pan.service.StorageKeyService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author Haylen
 * @date 2020-5-20
 */
@Service
public class StorageKeyServiceImpl implements StorageKeyService {

    /**
     * sha256
     */
    @Override
    public String getStorageKey(InputStream input) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("sha-256");
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = input.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, length);
        }
        input.close();
        byte[] sha256Bytes = messageDigest.digest();
        return new BigInteger(1, sha256Bytes).toString(16);
    }
}
