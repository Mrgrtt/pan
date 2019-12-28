package com.haylen.pan.service.impl;

import com.haylen.pan.entity.File;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.service.CatalogService;
import com.haylen.pan.service.FileService;
import com.haylen.pan.service.FileStorageService;
import com.haylen.pan.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * @author haylen
 * @date 2019-12-28
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private CatalogService catalogService;

    @Override
    public File upload(MultipartFile multipartFile, Long catalogId) {
        /* 该目录是否存在 */
        if (!catalogService.existed(catalogId)) {
            return null;
        }
        /* 同一目录下是否已存在该文件 */
        if (existed(catalogId, multipartFile.getOriginalFilename())) {
            return null;
        }
        String storageKey = fileStorageService.putFile(multipartFile);
        if (storageKey == null) {
            return null;
        }
        File file = new File();
        file.setCatalogId(catalogId);
        file.setOwnerId(ownerService.getCurrentOwner().getId());
        file.setStorageKey(storageKey);
        file.setName(multipartFile.getOriginalFilename());
        file.setMediaType(multipartFile.getContentType());
        file.setGmtCreate(LocalDateTime.now());
        file.setGmtModified(LocalDateTime.now());
        return fileRepository.save(file);
    }

    private boolean existed(Long catalogId, String name) {
        File file = fileRepository.findFileByCatalogIdAndName(catalogId, name);
        if (file == null) {
            return false;
        }
        return true;
    }
}