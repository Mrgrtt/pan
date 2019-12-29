package com.haylen.pan.service.impl;

import com.haylen.pan.entity.File;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.service.CatalogService;
import com.haylen.pan.service.FileService;
import com.haylen.pan.service.FileStorageService;
import com.haylen.pan.service.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author haylen
 * @date 2019-12-28
 */
@Service
@Slf4j
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
        file.setSize(multipartFile.getSize());
        file.setGmtCreate(LocalDateTime.now());
        file.setGmtModified(LocalDateTime.now());
        try {
            return fileRepository.save(file);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public String getFileMediaTypeByStorageKey(String key) {
        File file = fileRepository.findFileByStorageKey(key);
        return file == null ? null : file.getMediaType();
    }

    @Override
    public InputStream download(String key) {
        return fileStorageService.getFile(key);
    }

    @Override
    public List<File> listFile(Long catalogId) {
        return fileRepository.findFilesByCatalogId(catalogId);
    }

    @Override
    public int rename(String newName, Long id) {
        try {
            return fileRepository.rename(newName, id);
        } catch (Exception e) {
            log.info(e.getMessage());
            return 0;
        }
    }
}
