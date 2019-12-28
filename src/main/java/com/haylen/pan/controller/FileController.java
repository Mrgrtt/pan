package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.entity.File;
import com.haylen.pan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author haylen
 * @date 2019-12-28
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public CommonResult upload(@RequestPart(name = "file", required = true) MultipartFile mFile,
                               @RequestParam(name = "catalog_id", defaultValue = "0") Long catalogId) {
        File file = fileService.upload(mFile, catalogId);
        if (file == null) {
            return CommonResult.failed("文件上传失败");
        }
        return CommonResult.success(file, "文件上传成功");
    }
}
