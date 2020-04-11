package com.haylen.pan.domain.dto;

import lombok.Data;

/**
 * @author haylen
 * @date 2020-04-11
 */
@Data
public class SearchParam {
    private String keyWord;
    private Integer from;
    private Integer size;
}
