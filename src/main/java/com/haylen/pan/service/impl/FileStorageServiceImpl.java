package com.haylen.pan.service.impl;

import com.haylen.pan.exception.ApiException;
import com.haylen.pan.service.FileStorageService;
import com.haylen.pan.util.Sha256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author haylen
 * @date 2019-12-28
 */
@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${file-storage-path}")
    private String fileStoragePath;

    @Override
    public String putFile(MultipartFile multipartFile) {
        Path folderPath = Paths.get(fileStoragePath);
        String storageKey;
        try {
            storageKey = Sha256Util.encode(multipartFile.getInputStream());
        } catch (Exception e) {
            log.info("文件Sha256值计算失败", e);
            throw new ApiException("文件上传失败");
        }
        try {
            Path filePath = folderPath.resolve(storageKey);
            if (Files.notExists(folderPath)) {
                Files.createDirectory(folderPath);
            }
            if (Files.exists(filePath)) {
                return storageKey;
            }
            multipartFile.transferTo(filePath);
        } catch (IOException e) {
            log.info("文件本地保存失败", e);
            throw new ApiException("文件上传失败");
        }
        return storageKey;
    }

    @Override
    public InputStream getFile(String storageKey) {
        Path filePath = Paths.get(fileStoragePath, storageKey);
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new ApiException("文件下载失败");
        }
    }
}
