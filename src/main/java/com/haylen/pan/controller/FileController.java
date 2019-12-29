package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.entity.File;
import com.haylen.pan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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

    @RequestMapping("/download/{key}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String key) {
        InputStream inputStream = fileService.download(key);
        String mediaType = fileService.getFileMediaTypeByStorageKey(key);
        long length = 0;
        if (inputStream == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            length = inputStream.available();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        StreamingResponseBody streamingResponseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                byte[] bytes = new byte[1024];
                while (bufferedInputStream.read(bytes) != -1) {
                    outputStream.write(bytes);
                }
                outputStream.flush();
            }
        };
        return ResponseEntity.ok()
                .contentLength(length)
                .contentType(MediaType.valueOf(mediaType))
                .body(streamingResponseBody);
    }

    @RequestMapping("/list/{catalogId}")
    public CommonResult list(@PathVariable Long catalogId) {
        return CommonResult.success(fileService.listFile(catalogId));
    }
}
