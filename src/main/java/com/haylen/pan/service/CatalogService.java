package com.haylen.pan.service;

import com.haylen.pan.entity.Catalog;

import java.util.List;

/**
 * 目录服务
 * @author haylen
 * @date 2019-12-27
 */
public interface CatalogService {
    /**
     * 新建目录
     * @param parentId 父目录ID
     * @param name 目录名
     * @return 新建的目录
     */
    Catalog create(Long parentId, String name);
    /**
     * 获取子目录
     * @param id 目录id
     * @return 子目录
     */
    List<Catalog> listChildCatalog(Long id);

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
}
