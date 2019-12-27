package com.haylen.pan.repository;

import com.haylen.pan.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
     * 在指定目录下查找目录
     * @param parentId 父目录id
     * @param ownerId 用户id
     * @param name 要查找的目录名
     * @return 目录
     */
    Catalog findCatalogByParentIdAndOwnerIdAndName(Long parentId, Long ownerId, String name);

    /**
     * 移动目录到相应的父目录
     * @param id 目录id
     * @param newParentId 新的父目录的id
     * @return 修改结果
     */
    @Modifying
    @Transactional
    @Query("update Catalog c set c.parentId = ?1 where c.id = ?2")
    int changeParent(Long newParentId, Long id);
}
