package com.haylen.pan.service.impl;

import com.haylen.pan.service.FileStorageService;
import com.haylen.pan.service.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author haylen
 * @date 2019-12-28
 */
@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${file.storage.path}")
    private String fileStoragePath;
    @Autowired
    private OwnerService ownerService;

    @Override
    public String putFile(MultipartFile multipartFile) {
        Path catalogPath = Paths.get(
                fileStoragePath, ownerService.getCurrentOwnerId().toString());
        String storageKey = UUID.randomUUID().toString();
        Path filePath = catalogPath.resolve(storageKey);
        try {
            if (Files.notExists(catalogPath)) {
                Files.createDirectory(catalogPath);
            }
            multipartFile.transferTo(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        return storageKey;
    }

    @Override
    public InputStream getFile(String storageKey) {
        Long ownerId = ownerService.getCurrentOwnerId();
        Path filePath = Paths.get(fileStoragePath, ownerId.toString(), storageKey);
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
