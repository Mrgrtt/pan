package com.haylen.pan.service.impl;

import com.haylen.pan.entity.Folder;
import com.haylen.pan.entity.File;
import com.haylen.pan.repository.FolderRepository;
import com.haylen.pan.service.FolderService;
import com.haylen.pan.service.FileService;
import com.haylen.pan.service.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author haylen
 * @date 2019-12-27
 */
@Service
@Slf4j
public class FolderServiceImpl implements FolderService {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private FileService fileService;

    @Override
    public Folder create(Long parentId, String name){
        /* 父目录是否存在 */
        if (notExisted(parentId)) {
            return null;
        }
        Folder folder = new Folder();
        folder.setName(name);
        folder.setGmtModified(LocalDateTime.now());
        folder.setParentId(parentId);
        folder.setOwnerId(ownerService.getCurrentOwnerId());
        return folderRepository.save(folder);
    }

    @Override
    public List<Folder> listChildFolder(Long id) {
        return folderRepository.
                findFoldersByParentIdAndOwnerId(id,
                        ownerService.getCurrentOwnerId());
    }

    @Override
    public int move(Long newParentId, Long id){
        /* 禁止将自身设为其父目录 */
        if (newParentId.equals(id)) {
            return 0;
        }
        /* 该父目录是否存在 */
        if (notExisted(newParentId)) {
            return 0;
        }
        return folderRepository.updateParent(newParentId,
                LocalDateTime.now(), id, ownerService.getCurrentOwnerId());
    }

    @Override
    public int rename(String newName, Long id) {
        return folderRepository.updateName(newName,
                LocalDateTime.now(), id, ownerService.getCurrentOwnerId());
    }

    @Override
    public boolean notExisted(Long id) {
        Folder folder = folderRepository.findFolderByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (id !=0 && folder == null) {
            return true;
        }
        return false;
    }

    @Override
    public int delete(Long id) {
        Folder folder = folderRepository.findFolderByIdAndOwnerId(id, ownerService.getCurrentOwnerId());
        if (folder == null) {
            return 0;
        }
        List<File> files = fileService.listFile(id);
        for (File file: files) {
            fileService.delete(file.getId());
        }
        /* 递归删除子目录 */
        List<Folder> folders = listChildFolder(id);
        for (Folder child: folders) {
            delete(child.getId());
        }
        folderRepository.deleteById(id);
        return 1;
    }
}
