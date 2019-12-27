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
        /* 目录是否已存在 */
        Catalog catalog = catalogRepository.findCatalogByParentIdAndOwnerIdAndName(
                parentId, ownerService.getCurrentOwner().getId(), name);
        if (catalog != null) {
            return null;
        }
        /* 父目录是否存在 */
        if (parentId != 0) {
            Optional<Catalog> parent = catalogRepository.findById(parentId);
            if (!parent.isPresent()) {
                return null;
            }
        }
        catalog = new Catalog();
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
        if (newParentId != 0 && !catalogRepository.findById(newParentId).isPresent()) {
            return 0;
        }
        return catalogRepository.changeParent(newParentId, id);
    }
}
