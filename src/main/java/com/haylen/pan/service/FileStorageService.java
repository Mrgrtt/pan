package com.haylen.pan.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件储存
 * @author haylen
 * @date 2019-12-28
 */
public interface FileStorageService {
    /**
     * 存文件
     * @param multipartFile 文件
     * @return 储存key
     */
    String putFile(MultipartFile multipartFile);
    /**
     * 取文件
     * @param storageKey 储存key
     * @return 文件流
     */
    InputStream getFile(String storageKey);
}
