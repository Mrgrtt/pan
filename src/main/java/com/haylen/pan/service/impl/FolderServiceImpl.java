package com.haylen.pan.service.impl;

import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.repository.FileRepository;
import com.haylen.pan.repository.FolderRepository;
import com.haylen.pan.repository.OwnerRepository;
import com.haylen.pan.service.FolderService;
import com.haylen.pan.service.FileService;
import com.haylen.pan.service.OwnerService;
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
    private OwnerService ownerService;
    @Autowired
    private FileService fileService;

    @Override
    public Folder create(Long parentId, String name){
        /* 父目录不存在 */
        if (notExisted(parentId)) {
            throw new ApiException("父目录不存在");
        }
        /* 存在同名目录 */
        if (existedChildFolder(parentId, name)) {
            throw new ApiException("存在同名目录");
        }
        Folder folder = new Folder();
        folder.setName(name);
        folder.setGmtModified(LocalDateTime.now());
        folder.setParentId(parentId);
        folder.setOwnerId(ownerService.getCurrentOwnerId());
        folder.setStatus(0);
        return folderRepository.save(folder);
    }

    @Override
    public List<Folder> listChildFolder(Long id) {
        return folderRepository.
                listChildFolder(id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int move(Long newParentId, Long id){
        /* 禁止将自身设为其父目录 */
        if (newParentId.equals(id)) {
            throw new ApiException("禁止将自身设为其父目录");
        }
        /* 该父目录不存在 */
        if (notExisted(newParentId)) {
            throw new ApiException("父目录不存在");
        }
        Folder folder = getFolder(id);
        if(folder.getParentId().equals(newParentId)) {
            return 1;
        }
        /* 该目录是否已经存在 */
        if (existedChildFolder(newParentId, folder.getName())) {
            throw new ApiException("存在同名目录");
        }
        return folderRepository.updateParent(newParentId, id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int rename(String newName, Long id) {
        Folder folder = getFolder(id);
        if (folder.getName().equals(newName)) {
            return 1;
        }
        if (existedChildFolder(folder.getParentId(), newName)) {
            throw new ApiException("存在同名目录");
        }
        return folderRepository.updateName(newName, id, ownerService.getCurrentOwnerId());
    }

    @Override
    public boolean notExisted(Long id) {
        Optional<Folder> folderOptional =
                folderRepository.getFolder(id, ownerService.getCurrentOwnerId());
        if (id !=0 && !folderOptional.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        List<File> files = fileRepository.listAllStatusFile(id, ownerService.getCurrentOwnerId());
        for (File file: files) {
            fileService.delete(file.getId());
        }
        /* 递归删除子目录 */
        List<Folder> folders =
                folderRepository.listRecyclableFolder(id, ownerService.getCurrentOwnerId());
        for (Folder child: folders) {
            delete(child.getId());
        }
        folderRepository.updateStatus(id, ownerService.getCurrentOwnerId(), 2);
    }

    @Override
    public void copy(Long id, Long toFolderId) {
        Folder oldFolder = getFolder(id);
        if (notExisted(toFolderId)) {
            throw new ApiException("目标文件夹不存在");
        }
        if (existedChildFolder(toFolderId, oldFolder.getName())) {
            throw new ApiException("存在同名目录");
        }

        /*
         * 需要在这里先获取子文件夹列表，避免自我复制时（参数id == toFolderId）
         * 无限循环。
         */
        List<Folder> folders = listChildFolder(id);

        Folder folder = copy(toFolderId, oldFolder);
        folder = folderRepository.saveAndFlush(folder);

        /* 复制文件 */
        List<File> files = fileService.listFile(id);
        for (File file: files) {
            fileService.copy(folder.getId(), file.getId());
        }

        /* 复制子文件夹 */
        for (Folder f: folders) {
            copy(f.getId(), folder.getId());
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
    public Boolean existedChildFolder(Long id, String name) {
        Optional<Folder> folderOptional =
                folderRepository.getFolder(id, name, ownerService.getCurrentOwnerId());
        return folderOptional.isPresent();
    }

    @Override
    public void toRecycleBin(Long id) {
        getFolder(id);
        childFolderToRecycleBin(id);
        folderRepository.updateStatus(id, ownerService.getCurrentOwnerId(), 1);
    }

    private void childFolderToRecycleBin(Long id) {
        for (File file: fileService.listFile(id)) {
            ownerRepository.reduceUsedStorageSpace(file.getSize(), ownerService.getCurrentOwnerId());
            fileRepository.updateStatus(file.getId(), ownerService.getCurrentOwnerId(), 3);
        }
        for (Folder childFolder: listChildFolder(id)) {
            childFolderToRecycleBin(childFolder.getId());
        }
        folderRepository.updateStatus(id, ownerService.getCurrentOwnerId(), 3);
    }

    private Folder getFolder(Long id) {
        Optional<Folder> optionalFolder =
                folderRepository.getFolder(id, ownerService.getCurrentOwnerId());
        if (!optionalFolder.isPresent()) {
            throw new ApiException("不存在该文件夹");
        }
        return optionalFolder.get();
    }
}
