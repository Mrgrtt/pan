package com.haylen.pan.service;


/**
 * 缓存服务
 * @author haylen
 * @date 2020-03-23
 */
public interface CacheService {
    /**
     * 将对象写入缓存
     */
    <T> void setObject(String key, T o, long ms);

    /**
     * 获取缓存中的对象
     */
    <T> T getObject(String key);

    /**
     * 删除缓存中的对象
     */
    boolean delObject(String key);
}
