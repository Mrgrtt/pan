package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.entity.Folder;
import com.haylen.pan.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author  haylen
 * @date 2019-12-27
 */
@RestController
@RequestMapping("/folder")
public class FolderController {
    @Autowired
    private FolderService folderService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestParam(defaultValue = "0") Long parentId,
                               @RequestParam @NotEmpty String name) {
        Folder folder = folderService.create(parentId, name);
        if (folder == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(folder);
    }

    @RequestMapping(value = "/list/{id}")
    public CommonResult list(@PathVariable Long id) {
        List<Folder> list = folderService.listChildFolder(id);
        return CommonResult.success(list);
    }

    @RequestMapping(value = "/move/{id}", method = RequestMethod.POST)
    public CommonResult move(@RequestParam(defaultValue = "0") Long newParentId,
                             @PathVariable Long id) {
        if (folderService.move(newParentId, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @RequestMapping(value = "/rename/{id}", method = RequestMethod.POST)
    public CommonResult rename(@RequestParam @NotEmpty String newName,
                               @PathVariable Long id) {
        if (folderService.rename(newName, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        folderService.delete(id);
        return CommonResult.success();
    }

    @RequestMapping("/existedChildFolder/{id}")
    public CommonResult existedChildFolder(@PathVariable Long id,
                                           @RequestParam @NotEmpty String name) {
        return CommonResult.success(folderService.existedChildFolder(id, name));
    }
}
