package com.haylen.pan.service.impl;

import com.haylen.pan.entity.Catalog;
import com.haylen.pan.repository.CatalogRepository;
import com.haylen.pan.service.CatalogService;
import com.haylen.pan.service.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author haylen
 * @date 2019-12-27
 */
@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private OwnerService ownerService;

    @Override
    public Catalog create(Long parentId, String name) {
        /* 父目录是否存在 */
        if (!existed(parentId)) {
            return null;
        }
        Catalog catalog = new Catalog();
        catalog.setName(name);
        catalog.setGmtCreate(LocalDateTime.now());
        catalog.setGmtModified(LocalDateTime.now());
        catalog.setParentId(parentId);
        catalog.setOwnerId(ownerService.getCurrentOwner().getId());
        try {
            return catalogRepository.save(catalog);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
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
        try {
            return catalogRepository.changeParent(newParentId, LocalDateTime.now(), id);
        } catch (Exception e) {
            log.info(e.getMessage());
            return 0;
        }
    }

    @Override
    public int rename(String newName, Long id) {
        try {
            return catalogRepository.rename(newName, LocalDateTime.now(), id);
        } catch (Exception e) {
            log.info(e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean existed(Long id) {
        Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if (id !=0 && !optionalCatalog.isPresent()) {
            return false;
        }
        return true;
    }
}
