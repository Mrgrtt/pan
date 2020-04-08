package com.haylen.pan.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author haylen
 * @date 2020-04-01
 */
@Setter
@Getter
public class PageResult<T> {
    private List<T> data;
    private Long totalData;
    private Integer totalPages;
    private Integer pageNumber;
    private Integer pageSize;

    private PageResult(){
    };

    static public  <T> PageResult<T> of(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setData(page.getContent());
        result.setTotalData(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setPageNumber(page.getNumber());
        result.setPageSize(page.getSize());
        return result;
    }
}
