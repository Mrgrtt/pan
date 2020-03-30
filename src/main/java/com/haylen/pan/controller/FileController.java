package com.haylen.pan.controller;

import com.haylen.pan.domain.dto.CommonResult;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;

/**
 * @author haylen
 * @date 2019-12-28
 */
@RestController
@RequestMapping("/file")
@Api(tags = "FileController", value = "文件管理")
public class FileController {
    @Autowired
    private FileService fileService;

    @ApiOperation("上传文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public CommonResult upload(@RequestPart MultipartFile file,
                               @RequestParam(defaultValue = "0") Long folderId) {
        File result = fileService.upload(file, folderId);
        if (result == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(result);
    }

    @ApiOperation("下载文件（无需登陆)")
    @RequestMapping(value = "/download/{key}", method = RequestMethod.GET)
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String key,
                        @RequestHeader(name = "Range", required = false) String rangeHeader){
        InputStream inputStream = fileService.download(key);
        // 非网盘文件默认类型为图片，如上传的头像
        File file = fileService.getFileByStorageKey(key);
        if (file == null) {
            file = new File();
            file.setName("file");
            file.setMediaType(MediaType.IMAGE_JPEG_VALUE);
        }
        long fileLength = 0;
        String encodeFilename;

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

    @ApiOperation("获取文件夹下的文件列表")
    @RequestMapping(value = "/list/{folderId}", method = RequestMethod.GET)
    public CommonResult<List<File>> list(@PathVariable Long folderId) {
        return CommonResult.success(fileService.listFile(folderId));
    }

    @ApiOperation("重命名文件")
    @RequestMapping(value = "/rename/{id}", method = RequestMethod.POST)
    public CommonResult rename(@RequestParam @NotEmpty String newName,
                               @PathVariable Long id) {
        if (fileService.rename(newName, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("移动文件")
    @RequestMapping(value = "/move/{id}", method = RequestMethod.POST)
    public CommonResult move(@RequestParam Long newFolderId, @PathVariable Long id) {
        if (fileService.move(newFolderId, id) <= 0) {
            return CommonResult.failed();
        }
        return CommonResult.success();
    }

    @ApiOperation("删除文件")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id) {
        fileService.delete(id);
        return CommonResult.success();
    }

    @ApiOperation("指定目录下是否存在指定文件")
    @RequestMapping(value = "/existed", method = RequestMethod.GET)
    public CommonResult<Boolean> existed(@RequestParam Long folderId, @RequestParam @NotEmpty String name) {
        return CommonResult.success(fileService.isExisted(folderId, name));
    }

    @ApiOperation("复制文件")
    @RequestMapping(value = "/copy/{id}", method = RequestMethod.POST)
    public CommonResult copy(@RequestParam Long toFolderId, @PathVariable Long id) {
        File file = fileService.copy(toFolderId, id);
        if (file == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(file);

    }

}
