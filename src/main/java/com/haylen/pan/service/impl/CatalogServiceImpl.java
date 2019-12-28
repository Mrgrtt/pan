package com.haylen.pan.service.impl;

import com.haylen.pan.entity.Catalog;
import com.haylen.pan.repository.CatalogRepository;
import com.haylen.pan.service.CatalogService;
import com.haylen.pan.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author haylen
 * @date 2019-12-27
 */
@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private OwnerService ownerService;

    @Override
    public Catalog create(Long parentId, String name) {
        /* 父目录是否存在 */
        if (existed(parentId)) {
            return null;
        }
        /* 目录是否已存在 */
        if (existed(parentId, name)) {
            return null;
        }
        Catalog catalog = new Catalog();
        catalog.setName(name);
        catalog.setGmtCreate(LocalDateTime.now());
        catalog.setGmtModified(LocalDateTime.now());
        catalog.setParentId(parentId);
        catalog.setOwnerId(ownerService.getCurrentOwner().getId());
        return catalogRepository.save(catalog);
    }

    @Override
    public List<Catalog> listChildCatalog(Long id) {
        return catalogRepository.
                findCatalogsByParentIdAndOwnerId(id,
                        ownerService.getCurrentOwner().getId());
    }

    @Override
    public int move(Long newParentId, Long id) {
        /* 禁止将自身设为其父目录 */
        if (newParentId.equals(id)) {
            return 0;
        }
        /* 该父目录是否存在 */
        if (!existed(newParentId)) {
            return 0;
        }
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if (!optionalCatalog.isPresent()) {
            return 0;
        }
        /* 同一目录下，是否已存在该名称目录 */
        if (existed(newParentId, optionalCatalog.get().getName())) {
            return 0;
        }
        return catalogRepository.changeParent(newParentId, LocalDateTime.now(), id);
    }

    @Override
    public int rename(String newName, Long id) {
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if (!optionalCatalog.isPresent()) {
            return 0;
        }
        /* 同一目录下，是否已存在该名称目录 */
        if(existed(optionalCatalog.get().getParentId(), newName)) {
            return 0;
        }
        return catalogRepository.rename(newName, LocalDateTime.now(), id);
    }

    @Override
    public boolean existed(Long id) {
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if (id !=0 && !optionalCatalog.isPresent()) {
            return false;
        }
        return true;
    }

    private boolean existed(Long parentId, String name) {
        Catalog catalog = catalogRepository.findCatalogByParentIdAndOwnerIdAndName(
                parentId, ownerService.getCurrentOwner().getId(), name);
        return catalog == null ? false : true;
    }
}
