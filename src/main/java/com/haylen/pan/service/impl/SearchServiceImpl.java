package com.haylen.pan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.haylen.pan.domain.dto.SearchParam;
import com.haylen.pan.domain.entity.File;
import com.haylen.pan.domain.entity.Folder;
import com.haylen.pan.exception.ApiException;
import com.haylen.pan.service.OwnerService;
import com.haylen.pan.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索服务
 * @author haylen
 * @date 2020-04-11
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient esClient;
    @Autowired
    private OwnerService ownerService;
    private static final String ES_INDEX_FILE = "pan_file";
    private static final String ES_INDEX_FOLDER = "pan_folder";


    @Override
    public List<File> searchFile(SearchParam param) {
        SearchResponse response = search(param, ES_INDEX_FILE);
        return getResult(response, File.class);
    }

    @Override
    public List<Folder> searchFolder(SearchParam param) {
        SearchResponse response = search(param, ES_INDEX_FOLDER);
        return getResult(response, Folder.class);
    }

    private <T> List<T> getResult(SearchResponse response, Class<T> tClass) {
        SearchHits searchHits = response.getHits();
        ArrayList<T> results = new ArrayList<>();

        for (SearchHit hit: searchHits.getHits()) {
            T result = BeanUtil.mapToBean(hit.getSourceAsMap(), tClass, true);
            results.add(result);
        }
        return results;
    }

    private SearchResponse search(SearchParam param, String index) {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(getSearchSourceBuilder(param.getKeyWord(),
                ownerService.getCurrentOwnerId(), param.getFrom(), param.getSize()));
        SearchResponse response;
        try {
            response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new ApiException("文件搜索失败");
        }
        if (response.isTimedOut()) {
            throw new ApiException("文件搜索超时");
        }
        return response;
    }

    private SearchSourceBuilder getSearchSourceBuilder(String keyWord, Long ownerId, int from, int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("name", keyWord))
                        .must(QueryBuilders.constantScoreQuery(
                                QueryBuilders.termQuery("owner_id", ownerService.getCurrentOwnerId())
                        ))
        ).from(from).size(size);
        return searchSourceBuilder;
    }
}
