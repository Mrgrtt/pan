package com.haylen.pan.service;

import com.haylen.pan.entity.File;
import org.springframework.web.multipart.MultipartFile;

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
}
