package com.haylen.pan.service;

import com.haylen.pan.entity.Folder;

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
    Folder create(Long parentId, String name);

    /**
     * 获取子文件夹
     */
    List<Folder> listChildFolder(Long id);

    /**
     * 移动文件夹
     */
    int move(Long newParentId, Long id);

    /**
     * 重命名文件夹
     */
    int rename(String newName, Long id);

    /**
     * 文件夹是否存在
     */
    boolean notExisted(Long id);

    /**
     * 删除文件夹及其子文件夹和文件
     */
    void delete(Long id);

    /**
     * 复制目录下的所有子文件夹和文件
     * @param toFolderId 复制的目的目录
     */
    int copy(Long id, Long toFolderId);

    /**
     * 是否存在该名字的子文件夹
     */
    Boolean existedChildFolder(Long id, String name);
}
