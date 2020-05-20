package com.haylen.pan.service;

import com.haylen.pan.domain.entity.Folder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文件夹服务
 * @author haylen
 * @date 2019-12-27
 */
public interface FolderService {
    /**
     * 新建文件夹
     */
    Folder create(Long parentId, String name, Long ownerId);

    /**
     * 获取子文件夹
     */
    List<Folder> listChildFolder(Long id, Long ownerId);

    /**
     * 移动文件夹
     */
    int move(Long newParentId, Long id, Long ownerId);

    /**
     * 重命名文件夹
     */
    int rename(String newName, Long id, Long ownerId);

    /**
     * 文件夹是否存在
     */
    boolean notExisted(Long id, Long ownerId);

    /**
     * 删除文件夹及其子文件夹和文件
     */
    @Transactional(rollbackFor = Exception.class)
    void delete(Long id, Long ownerId);

    /**
     * 复制目录下的所有子文件夹和文件
     * @param toFolderId 复制的目的目录
     */
    @Transactional(rollbackFor = Exception.class)
    void copy(Long id, Long toFolderId, Long ownerId);

    /**
     * 是否存在该名字的子文件夹
     */
    Boolean existedChildFolder(Long id, String name, Long ownerId);

    /**
     * 放回收站
     */
    @Transactional(rollbackFor = Exception.class)
    void toRecycleBin(Long id, Long ownerId);
}
