package com.haylen.pan.controller;

import com.haylen.pan.dto.CommonResult;
import com.haylen.pan.entity.File;
import com.haylen.pan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
                               @RequestParam(defaultValue = "0") Long folderId) {
        File result = fileService.upload(file, folderId);
        if (result == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(result);
    }

    @RequestMapping("/download/{key}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String key,
                        @RequestHeader(name = "Range", required = false) String rangeHeader){
        InputStream inputStream = fileService.download(key);
        File file = fileService.getFileByStorageKey(key);
        long fileLength = 0;
        String encodeFilename;
        if (inputStream == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            fileLength = inputStream.available();
            encodeFilename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        long rangeStart = 0;
        long rangeEnd = fileLength;
        if (rangeHeader != null) {
            //example: bytes=0-1023 表示从0取到1023，长度为1024
            String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
            rangeStart = Long.parseLong(ranges[0]);
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]) + 1;
            }
        }
        final long skip = rangeStart;
        final long contentLength = rangeEnd - rangeStart;
        StreamingResponseBody streamingResponseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bufferedInputStream.skip(skip);
                byte[] bytes = new byte[1024];
                long readSum = 0;
                int readCount = 0;
                while ((readCount = bufferedInputStream.read(bytes)) != -1) {
                    if (readSum + readCount > contentLength) {
                        outputStream.write(bytes, 0, (int) (contentLength - readSum));
                    } else {
                        outputStream.write(bytes, 0, readCount);
                    }
                    readSum = readSum + readCount;
                }
                outputStream.flush();
                inputStream.close();
            }
        };
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Ranges", "bytes");
        headers.add("Content-Range", "bytes " + rangeStart + "-" + (rangeEnd - 1) + "/" + fileLength);
        headers.add("Content-Disposition",
                "attachment; filename=" + encodeFilename);
        return ResponseEntity.status(206)
                .contentLength(contentLength)
                .contentType(MediaType.valueOf(file.getMediaType()))
                .headers(headers)
                .body(streamingResponseBody);
    }

    @RequestMapping("/list/{folderId}")
    public CommonResult list(@PathVariable Long folderId) {
        return CommonResult.success(fileService.listFile(folderId));
    }

    @RequestMapping(value = "/rename/{id}", method = RequestMethod.POST)
    public CommonResult rename(@RequestParam @NotEmpty String newName,
                               @PathVariable Long id) {
        if (fileService.rename(newName, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @RequestMapping(value = "/move/{id}", method = RequestMethod.POST)
    public CommonResult move(@RequestParam Long newFolderId, @PathVariable Long id) {
        if (fileService.move(newFolderId, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        if (fileService.delete(id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @RequestMapping("/existed")
    public CommonResult existed(@RequestParam Long folderId, @RequestParam @NotEmpty String name) {
        return CommonResult.success(fileService.isExisted(folderId, name));
    }
}
