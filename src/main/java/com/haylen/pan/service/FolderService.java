package com.haylen.pan.service;

import com.haylen.pan.entity.Folder;

import java.util.List;

/**
 * 目录服务
 * @author haylen
 * @date 2019-12-27
 */
public interface FolderService {
    /**
     * 新建目录
     * @param parentId 父目录ID
     * @param name 目录名
     * @return 新建的目录
     */
    Folder create(Long parentId, String name);
    /**
     * 获取子目录
     * @param id 目录id
     * @return 子目录
     */
    List<Folder> listChildFolder(Long id);

    /**
     * 移动目录
     * @param newParentId 新的父目录
     * @param id 目录id
     * @return 移动结果
     */
    int move(Long newParentId, Long id);
    /**
     * 目录重命名
     * @param newName 新名
     * @param id 目录id
     * @return 结果
     */
    int rename(String newName, Long id);

    /**
     * 目录是否存在
     * @param id 目录id
     * @return 结果
     */
    boolean notExisted(Long id);

    /**
     * 删除目录及其子目录和文件
     * @param id
     * @return
     */
    int delete(Long id);
}
