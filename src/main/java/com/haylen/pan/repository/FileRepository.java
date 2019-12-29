package com.haylen.pan.repository;

import com.haylen.pan.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author haylen
 * @date 2019-12-25
 */
public interface FileRepository extends JpaRepository<File, Long> {
    /**
     * 在指定目录下根据文件名查找文件
     * @param catalogId 目录id
     * @param name 文件名
     * @return 文件
     */
    File findFileByCatalogIdAndName(Long catalogId, String name);
    /**
     * 根据储存key获取文件
     * @param storageKey 储存key
     * @return 文件
     */
    File findFileByStorageKey(String storageKey);

    /**
     * 获取指定目录下的文件
     * @param catalogId 目录id
     * @return 文件列表
     */
    List<File> findFilesByCatalogId(Long catalogId);
}
