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

import javax.validation.constraints.NotEmpty;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    public CommonResult upload(@RequestPart MultipartFile file,
                               @RequestParam(defaultValue = "0") Long catalogId) {
        File result = fileService.upload(file, catalogId);
        if (result == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(result);
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

    @RequestMapping(value = "/rename/{id}", method = RequestMethod.POST)
    public CommonResult rename(@RequestParam @NotEmpty String newName,
                               @PathVariable Long id) {
        if (fileService.rename(newName, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success("");
    }

    @RequestMapping(value = "/move/{id}", method = RequestMethod.POST)
    public CommonResult move(@RequestParam Long newCatalogId, @PathVariable Long id) {
        if (fileService.move(newCatalogId, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success("");
    }
}
