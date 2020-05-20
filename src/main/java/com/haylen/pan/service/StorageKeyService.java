package com.haylen.pan.service;

import java.io.InputStream;

/**
 * @author Haylen
 * @date 2020-5-20
 */
public interface StorageKeyService {
    /**
     * @param inputStream 文件流
     * @return 储存key
     */
    String getStorageKey(InputStream inputStream) throws Exception;
}
