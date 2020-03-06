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
        /* 文件是否已存在 */
        if (isExisted(folderId, multipartFile.getOriginalFilename())) {
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
        file.setDeleted(0);
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
        Optional<File> optionalFile = fileRepository
                .findFileByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (!optionalFile.isPresent()) {
            return 0;
        }
        if (optionalFile.get().getName().equals(newName)) {
            return 1;
        }
        if (isExisted(optionalFile.get().getFolderId(), newName)) {
            return 0;
        }
        return fileRepository.updateName(newName, id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int move(Long newFolderId, Long id) {
        if (folderService.notExisted(newFolderId)) {
            return 0;
        }
        Optional<File> optionalFile = fileRepository.
                findFileByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (!optionalFile.isPresent()) {
            return 0;
        }
        if (optionalFile.get().getOwnerId().equals(newFolderId)) {
            return 1;
        }
        if (isExisted(newFolderId, optionalFile.get().getName())) {
            return 0;
        }
        return fileRepository.updateFolderId(newFolderId, id, ownerService.getCurrentOwnerId());
    }

    @Override
    public void delete(Long id) {
        Optional<File> optionalFile = fileRepository.
                findFileByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (!optionalFile.isPresent()) {
            return;
        }
        fileRepository.delete(id, ownerService.getCurrentOwnerId());
    }

    @Override
    public boolean isExisted(Long folderId, String name) {
        Optional<File> optionalFile = fileRepository
                .findFileByFolderIdAndNameAndOwnerId(folderId, name, ownerService.getCurrentOwnerId());
        return optionalFile.isPresent();
    }

    @Override
    public File copy(Long toFolderId, Long id) {
        Optional<File> optionalFile = fileRepository.
                findFileByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (!optionalFile.isPresent()) {
            return null;
        }
        File oldFile = optionalFile.get();
        if (oldFile.getFolderId().equals(toFolderId)) {
            return oldFile;
        }
        /* 是否存在同名文件 */
        if (isExisted(toFolderId, oldFile.getName())) {
            return null;
        }
        File newFile = copy(toFolderId, oldFile);
        return fileRepository.save(newFile);
    }

    private File copy(Long folderId,File oldFile) {
        File newFile = new File();
        newFile.setGmtModified(LocalDateTime.now());
        newFile.setGmtCreate(LocalDateTime.now());
        newFile.setFolderId(folderId);
        newFile.setSize(oldFile.getSize());
        newFile.setDeleted(oldFile.getDeleted());
        newFile.setMediaType(oldFile.getMediaType());
        newFile.setStorageKey(oldFile.getStorageKey());
        newFile.setOwnerId(oldFile.getOwnerId());
        newFile.setName(oldFile.getName());
        return newFile;
    }
}
