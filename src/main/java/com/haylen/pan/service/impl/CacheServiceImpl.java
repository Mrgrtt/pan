package com.haylen.pan.service.impl;

import com.haylen.pan.service.CacheService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务实现
 * @author haylen
 * @date 2020-03-23
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public <T> void setObject(String key, T o, long ms) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        rBucket.set(o, ms, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> T getObject(String key) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        return rBucket.get();
    }

    @Override
    public boolean delObject(String key) {
        RBucket<Object> rBucket = redissonClient.getBucket(key);
        return rBucket.delete();
    }
}
