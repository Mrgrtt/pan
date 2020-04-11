package com.haylen.pan.controller;

import com.haylen.pan.domain.dto.CommonResult;
import com.haylen.pan.domain.dto.SearchParam;
import com.haylen.pan.domain.dto.SearchResult;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author haylen
 * @date 2020-04-11
 */
@RestController
@RequestMapping("/search")
@Api(tags = "searchController", value = "搜索")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @ApiOperation("文件搜索")
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public CommonResult<SearchResult<File>> searchFile(@RequestParam @NotBlank String keyWord,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "16") Integer size) {
        SearchParam searchParam = new SearchParam();
        searchParam.setFrom(from);
        searchParam.setKeyWord(keyWord);
        searchParam.setSize(size);

        SearchResult<File> searchResult = new SearchResult<>();
        searchResult.setFrom(from);
        searchResult.setPageSize(size);
        searchResult.setData(searchService.searchFile(searchParam));
        return  CommonResult.success(searchResult);
    }

    @ApiOperation("文件夹搜索")
    @RequestMapping(value = "/folder", method = RequestMethod.GET)
    public CommonResult<SearchResult<Folder>> searchFolder(@RequestParam @NotBlank String keyWord,
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @RequestParam(defaultValue = "16") Integer size) {
        SearchParam searchParam = new SearchParam();
        searchParam.setFrom(from);
        searchParam.setKeyWord(keyWord);
        searchParam.setSize(size);

        SearchResult<Folder> searchResult = new SearchResult<>();
        searchResult.setFrom(from);
        searchResult.setPageSize(size);
        searchResult.setData(searchService.searchFolder(searchParam));
        return  CommonResult.success(searchResult);
    }
}
