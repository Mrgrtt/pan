package com.haylen.pan.controller;

import com.haylen.pan.domain.dto.CommonResult;
import com.haylen.pan.domain.dto.FolderResult;
import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.service.FolderService;
import com.haylen.pan.service.OwnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  haylen
 * @date 2019-12-27
 */
@RestController
@RequestMapping("/folder")
@Api(tags = "FolderController", value = "文件夹管理")
public class FolderController {
    @Autowired
    private FolderService folderService;
    @Autowired
    private OwnerService ownerService;

    @ApiOperation("创建文件夹")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestParam(defaultValue = "0") Long parentId,
                               @RequestParam @NotEmpty String name) {
        Folder folder = folderService.create(parentId, name, ownerService.getCurrentOwnerId());
        if (folder == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(FolderResult.valueOf(folder));
    }

    @ApiOperation("获取子文件夹列表")
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public CommonResult<List<FolderResult>> list(@PathVariable Long id) {
        List<Folder> list = folderService.listChildFolder(id, ownerService.getCurrentOwnerId());
        List<FolderResult> folderResults = new ArrayList<>();
        for (Folder folder: list) {
            folderResults.add(FolderResult.valueOf(folder));
        }
        return CommonResult.success(folderResults);
    }

    @ApiOperation("移动文件夹")
    @RequestMapping(value = "/move/{id}", method = RequestMethod.POST)
    public CommonResult move(@RequestParam(defaultValue = "0") Long newParentId,
                             @PathVariable Long id) {
        if (folderService.move(newParentId, id, ownerService.getCurrentOwnerId()) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("重命名文件夹")
    @RequestMapping(value = "/rename/{id}", method = RequestMethod.POST)
    public CommonResult rename(@RequestParam @NotEmpty String newName,
                               @PathVariable Long id) {
        if (folderService.rename(newName, id, ownerService.getCurrentOwnerId()) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("删除文件夹")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        folderService.delete(id, ownerService.getCurrentOwnerId());
        return CommonResult.success();
    }

    @ApiOperation("是否存在指定子文件夹")
    @RequestMapping(value = "/existedChildFolder/{id}", method = RequestMethod.GET)
    public CommonResult<Boolean> existedChildFolder(@PathVariable Long id,
                                           @RequestParam @NotEmpty String name) {
        return CommonResult.success(folderService.existedChildFolder(id, name, ownerService.getCurrentOwnerId()));
    }

    @ApiOperation("复制文件夹")
    @RequestMapping(value = "/copy/{id}", method = RequestMethod.POST)
    public CommonResult copy(@PathVariable Long id, @RequestParam Long toFolderId) {
        folderService.copy(id, toFolderId, ownerService.getCurrentOwnerId());
        return CommonResult.success();
    }

    @ApiOperation("放到回收站")
    @RequestMapping(value = "/toRecycleBin/{id}", method = RequestMethod.POST)
    public CommonResult toRecycleBin(@PathVariable Long id) {
        folderService.toRecycleBin(id, ownerService.getCurrentOwnerId());
        return CommonResult.success();
    }
}
