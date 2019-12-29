package com.haylen.pan.repository;

import com.haylen.pan.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author haylen
 * @date 2019-12-25
 */
public interface FileRepository extends JpaRepository<File, Long> {
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

    /**
     * 重命名文件
     * @param newName 新名
     * @param id 文件id
     * @return 结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.name = ?1 where f.id = ?2")
    int rename(String newName, Long id);
}
