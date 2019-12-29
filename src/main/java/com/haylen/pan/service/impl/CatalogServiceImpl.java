package com.haylen.pan.service.impl;

import com.haylen.pan.entity.Catalog;
import com.haylen.pan.entity.File;
import com.haylen.pan.repository.CatalogRepository;
import com.haylen.pan.service.CatalogService;
import com.haylen.pan.service.FileService;
import com.haylen.pan.service.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private FileService fileService;

    @Override
    public Catalog create(Long parentId, String name){
        /* 父目录是否存在 */
        if (notExisted(parentId)) {
            return null;
        }
        Catalog catalog = new Catalog();
        catalog.setName(name);
        catalog.setGmtCreate(LocalDateTime.now());
        catalog.setGmtModified(LocalDateTime.now());
        catalog.setParentId(parentId);
        catalog.setOwnerId(ownerService.getCurrentOwnerId());
        return catalogRepository.save(catalog);
    }

    @Override
    public List<Catalog> listChildCatalog(Long id) {
        return catalogRepository.
                findCatalogsByParentIdAndOwnerId(id,
                        ownerService.getCurrentOwnerId());
    }

    @Override
    public int move(Long newParentId, Long id){
        /* 禁止将自身设为其父目录 */
        if (newParentId.equals(id)) {
            return 0;
        }
        /* 该父目录是否存在 */
        if (notExisted(newParentId)) {
            return 0;
        }
        return catalogRepository.changeParent(newParentId,
                LocalDateTime.now(), id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int rename(String newName, Long id) {
        return catalogRepository.rename(newName,
                LocalDateTime.now(), id, ownerService.getCurrentOwnerId());
    }

    @Override
    public boolean notExisted(Long id) {
        Catalog catalog = catalogRepository.findCatalogByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (id !=0 && catalog == null) {
            return true;
        }
        return false;
    }

    @Override
    public int delete(Long id) {
        Catalog catalog = catalogRepository.findCatalogByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (catalog == null) {
            return 0;
        }
        List<File> files = fileService.listFile(id);
        for (File file: files) {
            fileService.delete(file.getId());
        }
        /* 递归删除子目录 */
        List<Catalog> catalogs = listChildCatalog(id);
        for (Catalog child: catalogs) {
            delete(child.getId());
        }
        catalogRepository.deleteById(id);
        return 1;
    }
}
