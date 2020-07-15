package com.haylen.pan.service;

import com.haylen.pan.domain.entity.File;
import org.springframework.transaction.annotation.Transactional;
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
     * @param ownerId 用户id
     * @return 文件
     */
    @Transactional(rollbackFor = Exception.class)
    File upload(MultipartFile multipartFile, Long folderId, Long ownerId);

    /**
     * 获取文件
     * @param key 储存key
     * @param ownerId 用户id
     */
    File getFile(String key, long ownerId);

    /**
     * 下载文件
     * @param key 储存key
     * @return 文件流
     */
    InputStream download(String key);

    /**
     * 获取目录下的文件
     * @param folderId 目录id
     * @param ownerId 用户id
     * @return 文件列表
     */
    List<File> listFile(Long folderId, Long ownerId);

    /**
     * 重命名文件
     * @param newName 新文件名
     * @param id 文件id
     * @param ownerId 用户id
     * @return 结果
     */
    int rename(String newName, Long id, Long ownerId);

    /**
     * 移动文件
     * @param newFolderId 新目录id
     * @param id 文件id
     * @param ownerId 用户id
     * @return 结果
     */
    int move(Long newFolderId, Long id, Long ownerId);

    /**
     * 删除文件
     * @param id 文件id
     * @param ownerId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    void delete(Long id, Long ownerId);

    /**
     * 文件是否已存在
     * @param folderId 目录id
     * @param name 文件名
     * @param ownerId 用户id
     * @return 结果
     */
    boolean isExisted(Long folderId, String name, Long ownerId);

    /**
     * 复制
     * @param id 文件id
     * @param toFolderId 目标目录id
     * @param ownerId 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    File copy(Long toFolderId, Long id, Long ownerId);

    /**
     * 放到回收站
     * @param id 文件id
     * @param ownerId 用户id
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    int toRecycleBin(Long id, Long ownerId);

    /**
     * 检查并增加已用空间
     */
    void checkAndIncreaseUsedStorageSpace(Long expectedSize);
}
