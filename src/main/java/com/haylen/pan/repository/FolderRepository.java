package com.haylen.pan.repository;

import com.haylen.pan.entity.Folder;
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
public interface FolderRepository extends JpaRepository<Folder, Long> {
    /**
     * 获取指定目录的子文件夹
     */
    List<Folder> findFoldersByParentIdAndOwnerId(Long parentId, Long ownerId);

    /**
     * 更改父文件夹
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Folder c set c.parentId = ?1, c.gmtModified = current_timestamp where c.id = ?2 and c.ownerId = ?3")
    int updateParent(Long newParentId, Long id, Long ownerId);

    /**
     * 重命名
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Folder c set c.name = ?1, c.gmtModified = current_timestamp where c.id = ?2 and c.ownerId = ?3")
    int updateName(String newName, Long id, Long ownerId);

    /**
     * 查找文件夹通过id
     */
    Optional<Folder> findFolderByIdAndOwnerId(Long id, Long ownerId);

    /**
     * 是否存在该名字的子文件夹
     */
    Optional<Folder> findFolderByParentIdAndNameAndOwnerId(Long parentId, String name, Long ownerId);
}
