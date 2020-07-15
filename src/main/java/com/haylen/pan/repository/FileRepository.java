package com.haylen.pan.repository;

import com.haylen.pan.domain.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author haylen
 * @date 2019-12-25
 */
public interface FileRepository extends JpaRepository<File, Long> {
    /**
     * 根据id查找
     */
    @Query("select f from File f where f.id = ?1 and f.ownerId = ?2 and f.status = 0")
    Optional<File> findFileByIdAndOwnerId(Long id, Long ownerId);

    /**
     * 获取指定目录下的文件
     */
    @Query("select f from File f where f.folderId = ?1 and f.ownerId = ?2 and f.status = 0")
    List<File> findFilesByFolderIdAndOwnerId(Long folderId, Long ownerId);

    /**
     * 更新文件名
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.name = ?1, f.gmtModified = current_timestamp where f.id = ?2 and f.ownerId = ?3 and f.status = 0")
    int updateName(String newName, Long id, Long ownerId);

    /**
     * 更新文件夹id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.folderId = ?1, f.gmtModified = current_timestamp where f.id = ?2 and f.ownerId = ?3 and f.status = 0")
    int updateFolderId(Long newFolderId, Long id, Long ownerId);

    /**
     * 跟新文件状态
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.status = ?3, f.gmtModified = current_timestamp where f.id = ?1 and f.ownerId = ?2")
    int updateStatus(Long id, Long ownerId, Integer status);

    /**
     * 查找文件
     */
    @Query("select f from File f where f.folderId = ?1 and f.name = ?2 and f.ownerId = ?3 and f.status = 0")
    List<File> findFileByFolderIdAndNameAndOwnerId(Long folderId,  String name, Long ownerId);

    /**
     * 获取可回收文件列表
     */
    @Query("select f from File f where f.ownerId = ?1 and f.status = 1")
    Page<File> listRecyclableFile(Long ownerId, Pageable pageable);

    /**
     * 获取可回收的文件
     */
    @Query("select f from File f where f.id = ?1 and f.ownerId = ?2 and f.status = 1")
    Optional<File> getRecyclableFile(Long id, Long ownerId);

    /**
     * 获取可回收（回收站中不显示，将文件夹放入回收站，其子孙文件处于该状态）的文件列表
     */
    @Query("select f from File f where f.folderId = ?1 and f.ownerId = ?2 and f.status = 3")
    List<File> listRecyclableFile(Long folderId, Long ownerId);

    /**
     * 查找文件
     */
    @Query("select f from File f where f.id = ?1 and f.ownerId = ?2")
    Optional<File> getAnyStatusFile(Long id, Long ownerId);

    /**
     * 查找文件列表
     */
    @Query("select f from File f where f.folderId = ?1 and f.ownerId = ?2")
    List<File> listAnyStatusFile(Long folderId, Long ownerId);

    /**
     * @param key 存储key
     * @param ownerId 用户id
     * @return 文件
     */
    Optional<File> getFileByStorageKeyAndOwnerId(String key, long ownerId);
}
