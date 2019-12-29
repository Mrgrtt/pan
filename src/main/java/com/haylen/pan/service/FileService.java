package com.haylen.pan.service;

import com.haylen.pan.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件服务
 * @author haylen
 * @date 2019-12-28
 */
public interface FileService {
    /**
     * 上传文件
     * @param multipartFile 文件
     * @param catalogId 上传到的目录id
     * @return 文件
     */
    File upload(MultipartFile multipartFile, Long catalogId);

    /**
     * 根据储存key获取文件的媒体类型
     * @param key 储存key
     * @return 媒体类型
     */
    String getFileMediaTypeByStorageKey(String key);

    /**
     * 根据储存key下载文件
     * @param key 储存key
     * @return 文件流
     */
    InputStream download(String key);

    /**
     * 获取指定目录下的文件
     * @param catalogId 目录id
     * @return 文件列表
     */
    List<File> listFile(Long catalogId);
}
