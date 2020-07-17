package com.haylen.pan.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Owner;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.*;
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
    private FileRepository fileRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private CacheService cacheService;
    private final static long SHARE_FILE_EXPIRATION = 24 * 60 * 60 * 1000;

    @Override
    public File upload(MultipartFile multipartFile, Long folderId, Long ownerId) {
        /* 该目录是不存在 */
        if (folderService.notExisted(folderId, ownerId)) {
            throw new ApiException("该目录不存在");
        }
        /* 存在同命文件 */
        if (isExisted(folderId, multipartFile.getOriginalFilename(), ownerId)) {
            throw new ApiException("目录存在同名文件");
        }
        checkAndIncreaseUsedStorageSpace(multipartFile.getSize());
        String storageKey = fileStorageService.putFile(multipartFile);
        File file = new File();
        file.setFolderId(folderId);
        file.setOwnerId(ownerId);
        file.setStorageKey(storageKey);
        file.setName(multipartFile.getOriginalFilename());
        file.setMediaType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setGmtModified(LocalDateTime.now());
        file.setStatus(0);
        return fileRepository.save(file);
    }

    @Override
    public File getFile(long id, long ownerId) {
        Optional<File> o = fileRepository.findFileByIdAndOwnerId(id, ownerId);
        if (!o.isPresent()) {
            throw new ApiException("文件不存在");
        }
        return o.get();
    }

    @Override
    public InputStream download(String key) {
        return fileStorageService.getFile(key);
    }

    @Override
    public List<File> listFile(Long folderId, Long ownerId) {
        return fileRepository.findFilesByFolderIdAndOwnerId(
                folderId, ownerId);
    }

    @Override
    public int rename(String newName, Long id, Long ownerId) {
        File file = getFile(id, ownerId);
        if (file.getName().equals(newName)) {
            return 1;
        }
        if (isExisted(file.getFolderId(), newName, ownerId)) {
            throw new ApiException("所在目录存在同名文件");
        }
        return fileRepository.updateName(newName, id, ownerId);
    }

    @Override
    public int move(Long newFolderId, Long id, Long ownerId) {
        if (folderService.notExisted(newFolderId, ownerId)) {
            throw new ApiException("不存在该目录");
        }

        File file = getFile(id, ownerId);
        if (file.getOwnerId().equals(newFolderId)) {
            return 1;
        }
        if (isExisted(newFolderId, file.getName(), ownerId)) {
            throw new ApiException("目录存在同名文件");
        }
        return fileRepository.updateFolderId(newFolderId, id, ownerId);
    }

    @Override
    public void delete(Long id, Long ownerId) {
        Integer normalStatus = 0;
        Integer deletedStatus = 2;
        Optional<File> optionalFile =
                fileRepository.getAnyStatusFile(id, ownerId);
        if (!optionalFile.isPresent()) {
            throw new ApiException("文件不存在");
        }
        File file = optionalFile.get();
        if (file.getStatus().equals(deletedStatus)) {
            return;
        }
        /* 删除回收站中的文件不更新已用空间 */
        if (file.getStatus().equals(normalStatus)) {
            ownerRepository.reduceUsedStorageSpace(file.getSize(), ownerId);
        }
        fileRepository.updateStatus(id, ownerId, deletedStatus);
    }

    @Override
    public boolean isExisted(Long folderId, String name, Long ownerId) {
        List<File> files = fileRepository
                .findFileByFolderIdAndNameAndOwnerId(folderId, name, ownerId);
        return files.size() > 0;
    }

    @Override
    public File copy(Long toFolderId, Long id, Long ownerId) {
        if (folderService.notExisted(toFolderId, ownerId)) {
            throw new ApiException("目标文件夹不存在");
        }
        File oldFile = getFile(id, ownerId);
        if (oldFile.getFolderId().equals(toFolderId)) {
            return oldFile;
        }
        /* 是否存在同名文件 */
        if (isExisted(toFolderId, oldFile.getName(), ownerId)) {
            throw new ApiException("文件夹存在同名文件");
        }
        checkAndIncreaseUsedStorageSpace(oldFile.getSize());
        File newFile = copy(toFolderId, oldFile);
        return fileRepository.save(newFile);
    }

    @Override
    public int toRecycleBin(Long id, Long ownerId) {
        File file = getFile(id, ownerId);
        ownerRepository.reduceUsedStorageSpace(file.getSize(), ownerId);
        return fileRepository.updateStatus(id, ownerId, 1);
    }

    private File copy(Long folderId, File oldFile) {
        File newFile = new File();
        newFile.setGmtModified(LocalDateTime.now());
        newFile.setGmtCreate(LocalDateTime.now());
        newFile.setFolderId(folderId);
        newFile.setSize(oldFile.getSize());
        newFile.setStatus(oldFile.getStatus());
        newFile.setMediaType(oldFile.getMediaType());
        newFile.setStorageKey(oldFile.getStorageKey());
        newFile.setOwnerId(oldFile.getOwnerId());
        newFile.setName(oldFile.getName());
        return newFile;
    }

    @Override
    public void checkAndIncreaseUsedStorageSpace(Long expectedSize) {
        Owner owner = ownerService.getCurrentOwner();
        if (owner.getTotalStorageSpace() - ownerRepository.getUsedStorageSpace(owner.getId())
                < expectedSize) {
            throw new ApiException("存储空间不足");
        }
        ownerRepository.increaseUsedStorageSpace(expectedSize, owner.getId());
    }

    @Override
    public String share(long id, long ownerId) {
        File file = getFile(id, ownerId);
        String token = RandomUtil.randomString(5) + file.getId();
        cacheService.setObject(token, file.getId(), SHARE_FILE_EXPIRATION);
        return token;
    }

    @Override
    public File getFileByShareToken(String token) {
        Long id = (Long) cacheService.getObject(token);
        if(id == null) {
            throw new ApiException("分享文件已过期");
        }
        Optional<File> optionalFile = fileRepository.findById(id);
        if (!optionalFile.isPresent()) {
            throw new ApiException("分享的文件已被删除");
        }
        return optionalFile.get();
    }
}
