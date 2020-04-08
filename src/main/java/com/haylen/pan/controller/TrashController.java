package com.haylen.pan.controller;

import com.haylen.pan.domain.dto.CommonResult;
import com.haylen.pan.domain.dto.PageResult;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.service.TrashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 回收站控制器
 * @author haylen
 * @date 2020-04-01
 */
@Api(tags = "TrashController", value = "回收站管理")
@RestController
@RequestMapping("/trash")
public class TrashController {
    @Autowired
    private TrashService trashService;

    @ApiOperation("获取可回收文件列表")
    @RequestMapping(value = "/file/recyclable", method = RequestMethod.GET)
    public CommonResult<PageResult<File>> listDeletedFile(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "16") Integer pageSize) {
        Page<File> filePage = trashService.listRecyclableFile(pageNum, pageSize);
        return CommonResult.success(PageResult.of(filePage));
    }

    @ApiOperation("还原文件")
    @RequestMapping(value = "/file/recycle/{id}", method = RequestMethod.POST)
    public CommonResult recycleFile(@PathVariable Long id) {
        if (trashService.recycleFile(id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("获取可回收文件列表")
    @RequestMapping(value = "/folder/recyclable", method = RequestMethod.GET)
    public CommonResult<PageResult<Folder>> listDeletedFolder(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "16") Integer pageSize) {
        Page<Folder> folderPage = trashService.listRecyclableFolder(pageNum, pageSize);
        return CommonResult.success(PageResult.of(folderPage));
    }

    @ApiOperation("还原文件夹")
    @RequestMapping(value = "/folder/recycle/{id}", method = RequestMethod.POST)
    public CommonResult recycleFolder(@PathVariable Long id) {
        trashService.recycleFolder(id);
        return CommonResult.success();
    }
}
