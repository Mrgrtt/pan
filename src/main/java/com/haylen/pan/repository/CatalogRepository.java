package com.haylen.pan.repository;

import com.haylen.pan.entity.Catalog;
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
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    /**
     * 获取指定目录的子目录
     * @param parentId 父目录id
     * @param ownerId 用户id
     * @return 子目录
     */
    List<Catalog> findCatalogsByParentIdAndOwnerId(Long parentId, Long ownerId);
    /**
     * 移动目录到相应的父目录
     * @param id 目录id
     * @param newParentId 新的父目录的id
     * @param dateTime 时间
     * @param ownerId 用户id
     * @return 修改结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Catalog c set c.parentId = ?1, c.gmtModified = ?2 where c.id = ?3 and c.ownerId = ?4")
    int updateParent(Long newParentId, LocalDateTime dateTime, Long id, Long ownerId);

    /**
     * 目录重命名
     * @param newName 新名
     * @param dateTime 时间
     * @param id 目录id
     * @param ownerId 用户id
     * @return 结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Catalog c set c.name = ?1, c.gmtModified = ?2 where c.id = ?3 and c.ownerId = ?4")
    int updateName(String newName, LocalDateTime dateTime, Long id, Long ownerId);

    /**
     * 查找目录
     * @param id 目录id
     * @param ownerId 用户id
     * @return 结果
     */
    Catalog findCatalogByIdAndOwnerId(Long id, Long ownerId);
}
