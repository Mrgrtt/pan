package com.haylen.pan.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author haylen
 * @date 2019-4-11
 */
@Data
public class SearchResult<T> {
    private Integer from;
    private Integer pageSize;
    private List<T> data;
}
