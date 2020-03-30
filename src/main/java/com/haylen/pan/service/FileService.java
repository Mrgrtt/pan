package com.haylen.pan.service;

import com.haylen.pan.domain.entity.File;
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
     * @param folderId 上传到的目录id
     * @return 文件
     */
    File upload(MultipartFile multipartFile, Long folderId);

    /**
     * 根据储存key获取文件
     * @param key 储存key
     * @return 文件
     */
    File getFileByStorageKey(String key);

    /**
     * 根据储存key下载文件
     * @param key 储存key
     * @return 文件流
     */
    InputStream download(String key);

    /**
     * 获取指定目录下的文件
     * @param folderId 目录id
     * @return 文件列表
     */
    List<File> listFile(Long folderId);
    /**
     * 重命名文件
     * @param newName 新名
     * @param id 文件id
     * @return 结果
     */
    int rename(String newName, Long id);

    /**
     * 移动文件
     * @param newFolderId 新目录id
     * @param id 文件id
     * @return 结果
     */
    int move(Long newFolderId, Long id);

    /**
     * 删除文件
     * @param id 文件id
     * @return 结果
     */
    void delete(Long id);

    /**
     * 文件是否已存在
     */
    boolean isExisted(Long folderId, String name);

    /**
     * 复制
     */
    File copy(Long toFolderId, Long id);
}
