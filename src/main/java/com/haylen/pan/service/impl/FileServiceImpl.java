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
import java.util.Optional;

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
        if (catalogService.notExisted(catalogId)) {
            return null;
        }
        String storageKey = fileStorageService.putFile(multipartFile);
        if (storageKey == null) {
            return null;
        }
        File file = new File();
        file.setCatalogId(catalogId);
        file.setOwnerId(ownerService.getCurrentOwnerId());
        file.setStorageKey(storageKey);
        file.setName(multipartFile.getOriginalFilename());
        file.setMediaType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setGmtCreate(LocalDateTime.now());
        file.setGmtModified(LocalDateTime.now());
        return fileRepository.save(file);
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
        return fileRepository.findFilesByCatalogIdAndOwnerId(
                catalogId, ownerService.getCurrentOwnerId());
    }

    @Override
    public int rename(String newName, Long id) {
        return fileRepository.updateName(newName,
                LocalDateTime.now(), id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int move(Long newCatalogId, Long id) {
        if (catalogService.notExisted(newCatalogId)) {
            return 0;
        }
        return fileRepository.updateCatalog(newCatalogId,
                LocalDateTime.now(), id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int delete(Long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if (!optionalFile.isPresent()) {
            return 0;
        }
        if (fileRepository.delete(id, ownerService.getCurrentOwnerId()) > 0) {
            fileStorageService.deleteFile(optionalFile.get().getStorageKey());
            return 1;
        }
        return 0;
    }
}
