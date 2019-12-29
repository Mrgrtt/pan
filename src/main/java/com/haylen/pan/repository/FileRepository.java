package com.haylen.pan.repository;

import com.haylen.pan.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
     * @param ownerId 用户id
     * @return 文件列表
     */
    List<File> findFilesByCatalogIdAndOwnerId(Long catalogId, Long ownerId);

    /**
     * 重命名文件
     * @param newName 新名
     * @param time 时间
     * @param id 文件id
     * @param ownerId 用户id
     * @return 结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.name = ?1, f.gmtModified = ?2 where f.id = ?3 and f.ownerId = ?4")
    int rename(String newName, LocalDateTime time, Long id, Long ownerId);

    /**
     * 移动文件
     * @param newCatalogId 新目录id
     * @param time 时间
     * @param id 文件id
     * @param ownerId 用户id
     * @return 结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.catalogId = ?1, f.gmtModified = ?2 where f.id = ?3 and f.ownerId = ?4")
    int move(Long newCatalogId, LocalDateTime time, Long id, Long ownerId);

    /**
     * 删除文件
     * @param id 文件id
     * @param ownerId 用户id
     * @return 结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete File f where f.id = ?1 and f.ownerId = ?2")
    int delete(Long id, Long ownerId);
}
