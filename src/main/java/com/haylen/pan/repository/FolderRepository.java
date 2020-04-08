package com.haylen.pan.repository;

import com.haylen.pan.domain.entity.Folder;
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
public interface FolderRepository extends JpaRepository<Folder, Long> {
    /**
     * 获取指定文件夹的子文件夹
     */
    @Query("select f from Folder f where f.parentId = ?1 and f.ownerId = ?2 and f.status = 0")
    List<Folder> listChildFolder(Long parentId, Long ownerId);

    /**
     * 更改父文件夹
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Folder c set c.parentId = ?1, c.gmtModified = current_timestamp where c.id = ?2 and c.ownerId = ?3 and c.status = 0")
    int updateParent(Long newParentId, Long id, Long ownerId);

    /**
     * 重命名
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Folder c set c.name = ?1, c.gmtModified = current_timestamp where c.id = ?2 and c.ownerId = ?3 and c.status = 0")
    int updateName(String newName, Long id, Long ownerId);

    /**
     * 查找文件夹通过id
     */
    @Query("select f from Folder f where f.id = ?1 and f.ownerId = ?2 and f.status = 0")
    Optional<Folder> getFolder(Long id, Long ownerId);

    /**
     * 是否存在该名字的子文件夹
     */
    @Query("select f from Folder f where f.parentId = ?1 and f.name = ?2 and f.ownerId = ?3 and f.status = 0")
    Optional<Folder> getFolder(Long parentId, String name, Long ownerId);

    /**
     * 删除
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Folder f set f.status = ?3, f.gmtModified = current_timestamp where f.id = ?1 and f.ownerId = ?2")
    void updateStatus(Long id, Long ownerId, Integer status);

    /**
     * 获取可回收文件夹列表
     */
    @Query("select f from Folder f where f.ownerId = ?1 and f.status = 1")
    Page<Folder> listRecyclableFolder(Long ownerId, Pageable pageable);

    /**
     * 获取可回收的文件夹
     */
    @Query("select f from Folder f where f.id = ?1 and f.ownerId = ?2 and f.status = 1")
    Optional<Folder> getRecyclableFolder(Long id, Long ownerId);

    /**
     * 获取可回收（回收站中不显示，将文件夹放入回收站，其子孙文件夹处于该状态）的子文件列表
     */
    @Query("select f from Folder f where f.parentId = ?1 and f.ownerId = ?2 and f.status = 3")
    List<Folder> listRecyclableFolder(Long parentId, Long ownerId);

    /**
     * 查找文件列表
     */
    @Query("select f from Folder f where f.parentId = ?1 and f.ownerId = ?2")
    List<Folder> listAllStatusFolder(Long parentId, Long ownerId);
}
