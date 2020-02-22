package com.haylen.pan.service.impl;

import com.haylen.pan.entity.File;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.service.FolderService;
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
    private FolderService folderService;

    @Override
    public File upload(MultipartFile multipartFile, Long folderId) {
        /* 该目录是否存在 */
        if (folderService.notExisted(folderId)) {
            return null;
        }
        String storageKey = fileStorageService.putFile(multipartFile);
        if (storageKey == null) {
            return null;
        }
        File file = new File();
        file.setFolderId(folderId);
        file.setOwnerId(ownerService.getCurrentOwnerId());
        file.setStorageKey(storageKey);
        file.setName(multipartFile.getOriginalFilename());
        file.setMediaType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setGmtModified(LocalDateTime.now());
        return fileRepository.save(file);
    }

    @Override
    public File getFileByStorageKey(String key) {
        return fileRepository.findFileByStorageKey(key);
    }

    @Override
    public InputStream download(String key) {
        return fileStorageService.getFile(key);
    }

    @Override
    public List<File> listFile(Long folderId) {
        return fileRepository.findFilesByFolderIdAndOwnerId(
                folderId, ownerService.getCurrentOwnerId());
    }

    @Override
    public int rename(String newName, Long id) {
        return fileRepository.updateName(newName,
                LocalDateTime.now(), id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int move(Long newFolderId, Long id) {
        if (folderService.notExisted(newFolderId)) {
            return 0;
        }
        return fileRepository.updateFolder(newFolderId,
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
