package com.haylen.pan.service;

import com.haylen.pan.domain.dto.SearchParam;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Folder;

import java.util.List;

/**
 * 搜索服务
 * @author haylen
 * @date 2020-04-11
 */
public interface SearchService {
    /**
     * 搜索文件
     */
    List<File> searchFile(SearchParam searchParam);

    /**
     * 搜索文件夹
     */
    List<Folder> searchFolder(SearchParam searchParam);
}
