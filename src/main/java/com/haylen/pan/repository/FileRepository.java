package com.haylen.pan.repository;

import com.haylen.pan.entity.File;
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
    @Query("select f from File f where f.id = ?1 and f.ownerId = ?2 and f.deleted = 0")
    Optional<File> findFileByIdAndOwnerId(Long id, Long ownerId);

    /**
     * 根据储存key获取文件
     * @param storageKey 储存key
     */
    File findFileByStorageKey(String storageKey);

    /**
     * 获取指定目录下的文件
     */
    @Query("select f from File f where f.folderId = ?1 and f.ownerId = ?2 and f.deleted = 0")
    List<File> findFilesByFolderIdAndOwnerId(Long folderId, Long ownerId);

    /**
     * 更新文件名
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.name = ?1, f.gmtModified = current_timestamp where f.id = ?2 and f.ownerId = ?3")
    int updateName(String newName, Long id, Long ownerId);

    /**
     * 更新文件夹id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.folderId = ?1, f.gmtModified = current_timestamp where f.id = ?2 and f.ownerId = ?3")
    int updateFolderId(Long newFolderId, Long id, Long ownerId);

    /**
     * 删除文件
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.deleted = 1 where f.id = ?1 and f.ownerId = ?2")
    int delete(Long id, Long ownerId);

    /**
     * 查找文件
     */
    @Query("select f from File f where f.folderId = ?1 and f.name = ?2 and f.ownerId = ?3 and f.deleted = 0")
    Optional<File> findFileByFolderIdAndNameAndOwnerId(Long folderId,  String name, Long ownerId);
}
