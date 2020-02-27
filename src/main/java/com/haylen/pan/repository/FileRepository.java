package com.haylen.pan.repository;

import com.haylen.pan.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author haylen
 * @date 2019-12-25
 */
public interface FileRepository extends JpaRepository<File, Long> {
    /**
     * 根据储存key获取文件
     * @param storageKey 储存key
     */
    File findFileByStorageKey(String storageKey);

    /**
     * 获取指定目录下的文件
     */
    List<File> findFilesByFolderIdAndOwnerId(Long folderId, Long ownerId);

    /**
     * 重命名文件
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.name = ?1, f.gmtModified = ?2 where f.id = ?3 and f.ownerId = ?4")
    int updateName(String newName, LocalDateTime time, Long id, Long ownerId);

    /**
     * 修改文件夹
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update File f set f.folderId = ?1, f.gmtModified = ?2 where f.id = ?3 and f.ownerId = ?4")
    int updateFolder(Long newFolderId, LocalDateTime time, Long id, Long ownerId);

    /**
     * 删除文件
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from File f where f.id = ?1 and f.ownerId = ?2")
    int delete(Long id, Long ownerId);

    /**
     * 查找文件
     */
    Optional<File> findFileByFolderIdAndOwnerIdAndName(Long folderId, Long ownerId, String name);
}
