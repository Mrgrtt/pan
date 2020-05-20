package com.haylen.pan.service.impl;

import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.repository.FolderRepository;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.FolderService;
import com.haylen.pan.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 文件夹服务
 * @author haylen
 * @date 2019-12-27
 */
@Service
@Slf4j
public class FolderServiceImpl implements FolderService {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private FileService fileService;

    @Override
    public Folder create(Long parentId, String name, Long ownerId){
        /* 父目录不存在 */
        if (notExisted(parentId, ownerId)) {
            throw new ApiException("父目录不存在");
        }
        /* 存在同名目录 */
        if (existedChildFolder(parentId, name, ownerId)) {
            throw new ApiException("存在同名目录");
        }
        Folder folder = new Folder();
        folder.setName(name);
        folder.setGmtModified(LocalDateTime.now());
        folder.setParentId(parentId);
        folder.setOwnerId(ownerId);
        folder.setStatus(0);
        return folderRepository.save(folder);
    }

    @Override
    public List<Folder> listChildFolder(Long id, Long ownerId) {
        return folderRepository.
                listChildFolder(id, ownerId);
    }

    @Override
    public int move(Long newParentId, Long id, Long ownerId){
        /* 禁止将自身设为其父目录 */
        if (newParentId.equals(id)) {
            throw new ApiException("禁止将自身设为其父目录");
        }
        /* 该父目录不存在 */
        if (notExisted(newParentId, ownerId)) {
            throw new ApiException("父目录不存在");
        }
        Folder folder = getFolder(id, ownerId);
        if(folder.getParentId().equals(newParentId)) {
            return 1;
        }
        /* 该目录是否已经存在 */
        if (existedChildFolder(newParentId, folder.getName(), ownerId)) {
            throw new ApiException("存在同名目录");
        }
        return folderRepository.updateParent(newParentId, id, ownerId);
    }

    @Override
    public int rename(String newName, Long id, Long ownerId) {
        Folder folder = getFolder(id, ownerId);
        if (folder.getName().equals(newName)) {
            return 1;
        }
        if (existedChildFolder(folder.getParentId(), newName, ownerId)) {
            throw new ApiException("存在同名目录");
        }
        return folderRepository.updateName(newName, id, ownerId);
    }

    @Override
    public boolean notExisted(Long id, Long ownerId) {
        Optional<Folder> folderOptional =
                folderRepository.getFolder(id, ownerId);
        if (id !=0 && !folderOptional.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id, Long ownerId) {
        List<File> files = fileRepository.listAnyStatusFile(id, ownerId);
        for (File file: files) {
            fileService.delete(file.getId(), ownerId);
        }
        /* 递归删除子目录 */
        List<Folder> folders =
                folderRepository.listAnyStatusFolder(id, ownerId);
        for (Folder child: folders) {
            delete(child.getId(), ownerId);
        }
        folderRepository.updateStatus(id, ownerId, 2);
    }

    @Override
    public void copy(Long id, Long toFolderId, Long ownerId) {
        Folder oldFolder = getFolder(id, ownerId);
        if (notExisted(toFolderId, ownerId)) {
            throw new ApiException("目标文件夹不存在");
        }
        if (existedChildFolder(toFolderId, oldFolder.getName(), ownerId)) {
            throw new ApiException("存在同名目录");
        }

        /*
         * 需要在这里先获取子文件夹列表，避免自我复制时（参数id == toFolderId）
         * 无限循环。
         */
        List<Folder> folders = listChildFolder(id, ownerId);

        Folder folder = copy(toFolderId, oldFolder);
        folder = folderRepository.saveAndFlush(folder);

        /* 复制文件 */
        List<File> files = fileService.listFile(id, ownerId);
        for (File file: files) {
            fileService.copy(folder.getId(), file.getId(), ownerId);
        }

        /* 复制子文件夹 */
        for (Folder f: folders) {
            copy(f.getId(), folder.getId(), ownerId);
        }
    }

    private Folder copy(Long parentId, Folder oldFolder) {
        Folder folder = new Folder();
        folder.setName(oldFolder.getName());
        folder.setOwnerId(oldFolder.getOwnerId());
        folder.setParentId(parentId);
        folder.setGmtCreate(LocalDateTime.now());
        folder.setGmtModified(LocalDateTime.now());
        folder.setStatus(oldFolder.getStatus());
        return folder;
    }

    @Override
    public Boolean existedChildFolder(Long id, String name, Long ownerId) {
        List<Folder> folders =
                folderRepository.getFolders(id, name, ownerId);
        return folders.size() > 0;
    }

    @Override
    public void toRecycleBin(Long id, Long ownerId) {
        getFolder(id, ownerId);
        childFolderToRecycleBin(id, ownerId);
        folderRepository.updateStatus(id, ownerId, 1);
    }

    private void childFolderToRecycleBin(Long id, Long ownerId) {
        for (File file: fileService.listFile(id, ownerId)) {
            ownerRepository.reduceUsedStorageSpace(file.getSize(), ownerId);
            fileRepository.updateStatus(file.getId(), ownerId, 3);
        }
        for (Folder childFolder: listChildFolder(id, ownerId)) {
            childFolderToRecycleBin(childFolder.getId(), ownerId);
        }
        folderRepository.updateStatus(id, ownerId, 3);
    }

    private Folder getFolder(Long id, Long ownerId) {
        Optional<Folder> optionalFolder =
                folderRepository.getFolder(id, ownerId);
        if (!optionalFolder.isPresent()) {
            throw new ApiException("不存在该文件夹");
        }
        return optionalFolder.get();
    }
}
