package com.haylen.pan.service.impl;

import com.haylen.pan.entity.File;
import com.haylen.pan.service.FileService;
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
    @Autowired
    private FileService fileService;

    @Override
    public String putFile(MultipartFile multipartFile) {
        Path folderPath = Paths.get(
                fileStoragePath, ownerService.getCurrentOwnerId().toString());
        String storageKey = UUID.randomUUID().toString();
        Path filePath = folderPath.resolve(storageKey);
        try {
            if (Files.notExists(folderPath)) {
                Files.createDirectory(folderPath);
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
        File file = fileService.getFileByStorageKey(storageKey);
        if (file == null) {
            return null;
        }
        Path filePath = Paths.get(fileStoragePath, file.getOwnerId().toString(), storageKey);
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteFile(String storageKey) {
        Long ownerId = ownerService.getCurrentOwnerId();
        Path path = Paths.get(fileStoragePath, ownerId.toString(), storageKey);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }
}
