package com.haylen.pan.service;

import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Folder;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 回收站服务
 * @author haylen
 * @date 2020-04-01
 */
public interface TrashService {
    /**
     * 获取可回收文件列表
     */
    Page<File> listRecyclableFile(int pageNum, int pageSize, Long ownerId);

    /**
     * 还原文件
     */
    @Transactional(rollbackFor = Exception.class)
    int recycleFile(Long id, Long ownerId);

    /**
     * 获取可回收文件夹列表
     */
    Page<Folder> listRecyclableFolder(int pageNum, int pageSize, Long ownerId);

    /**
     * 还原文件夹
     */
    @Transactional(rollbackFor = Exception.class)
    void recycleFolder(Long id, Long ownerId);
}
