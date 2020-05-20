package com.haylen.pan.service.impl;

import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.repository.FolderRepository;
import com.haylen.pan.service.FileService;
import com.haylen.pan.service.FolderService;
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
    private FileService fileService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderRepository folderRepository;

    @Override
    public Page<File> listRecyclableFile(int pageNum, int pageSize, Long ownerId) {
        Sort sort = new Sort(Sort.Direction.DESC, "gmtModified");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return fileRepository.listRecyclableFile(ownerId, pageable);
    }

    @Override
    public int recycleFile(Long id, Long ownerId) {
        Optional<File> optionalFile = fileRepository
                .getRecyclableFile(id, ownerId);
        if (!optionalFile.isPresent()) {
            throw new ApiException("回收站不存在该文件");
        }
        File file = optionalFile.get();
        /* 如果原目录已被删除，默认还原到根目录 */
        if (folderService.notExisted(file.getFolderId(), ownerId)) {
            file.setFolderId(0L);
        }

        if (fileService.isExisted(file.getFolderId(), file.getName(), ownerId)) {
            throw new ApiException("原来的文件夹存在同名文件");
        }
        return recycleFile(file, ownerId);
    }

    private int recycleFile(File file, Long ownerId) {
        fileService.checkAndIncreaseUsedStorageSpace(file.getSize());
        return fileRepository.updateStatus(file.getId(), ownerId, 0);
    }

    @Override
    public Page<Folder> listRecyclableFolder(int pageNum, int pageSize, Long ownerId) {
        Sort sort = new Sort(Sort.Direction.DESC, "gmtModified");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return folderRepository.listRecyclableFolder(ownerId, pageable);
    }

    @Override
    public void recycleFolder(Long id, Long ownerId) {
        Optional<Folder> optionalFolder = folderRepository
                .getRecyclableFolder(id, ownerId);
        if (!optionalFolder.isPresent()) {
            throw new ApiException("回收站不存在该文件夹");
        }
        if (folderService.existedChildFolder(id, optionalFolder.get().getName(), ownerId)) {
            throw new ApiException("原来的文件夹存在同名子文件夹");
        }
        recycleFolder(optionalFolder.get(), ownerId);
    }

    private void recycleFolder(Folder folder, Long ownerId) {
        folderRepository.updateStatus(folder.getId(), ownerId, 0);
        for (File file: fileRepository.listRecyclableFile(folder.getId(),
                ownerId)) {
            recycleFile(file, ownerId);
        }
        for (Folder childFolder: folderRepository.listRecyclableFolder(
                folder.getId(), ownerId)) {
            recycleFolder(childFolder, ownerId);
        }
    }
}
