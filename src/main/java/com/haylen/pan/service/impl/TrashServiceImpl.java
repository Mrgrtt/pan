package com.haylen.pan.service.impl;

import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.repository.FolderRepository;
import com.haylen.pan.service.FileService;
import com.haylen.pan.service.FolderService;
import com.haylen.pan.service.OwnerService;
import com.haylen.pan.service.TrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 回收站服务
 * @author haylen
 * @date 2020-04-01
 */
@Service
public class TrashServiceImpl implements TrashService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderRepository folderRepository;

    @Override
    public Page<File> listRecyclableFile(int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "gmtModified");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return fileRepository.listRecyclableFile(ownerService.getCurrentOwnerId(), pageable);
    }

    @Override
    public int recycleFile(Long id) {
        Optional<File> optionalFile = fileRepository
                .getRecyclableFile(id, ownerService.getCurrentOwnerId());
        if (!optionalFile.isPresent()) {
            throw new ApiException("回收站不存在该文件");
        }
        File file = optionalFile.get();
        /* 如果原目录已被删除，默认还原到根目录 */
        if (folderService.notExisted(file.getFolderId())) {
            file.setFolderId(0L);
        }

        if (fileService.isExisted(file.getFolderId(), file.getName())) {
            throw new ApiException("原来的文件夹存在同名文件");
        }
        return recycleFile(file);
    }

    private int recycleFile(File file) {
        fileService.checkAndIncreaseUsedStorageSpace(file.getSize());
        return fileRepository.updateStatus(file.getId(), ownerService.getCurrentOwnerId(), 0);
    }

    @Override
    public Page<Folder> listRecyclableFolder(int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "gmtModified");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return folderRepository.listRecyclableFolder(ownerService.getCurrentOwnerId(), pageable);
    }

    @Override
    public void recycleFolder(Long id) {
        Optional<Folder> optionalFolder = folderRepository
                .getRecyclableFolder(id, ownerService.getCurrentOwnerId());
        if (!optionalFolder.isPresent()) {
            throw new ApiException("回收站不存在该文件夹");
        }
        if (folderService.existedChildFolder(id, optionalFolder.get().getName())) {
            throw new ApiException("原来的文件夹存在同名子文件夹");
        }
        recycleFolder(optionalFolder.get());
    }

    private void recycleFolder(Folder folder) {
        folderRepository.updateStatus(folder.getId(), ownerService.getCurrentOwnerId(), 0);
        for (File file: fileRepository.listRecyclableFile(folder.getId(),
                ownerService.getCurrentOwnerId())) {
            recycleFile(file);
        }
        for (Folder childFolder: folderRepository.listRecyclableFolder(
                folder.getId(), ownerService.getCurrentOwnerId())) {
            recycleFolder(childFolder);
        }
    }
}
