package com.mateo.urlshortener.mapper;

import com.mateo.urlshortener.model.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PagedResponseMapper {
    public <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return new PagedResponse<>(
                new PagedResponse.meta(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalPages(),
                        page.getTotalElements()
                ),
                page.getContent()
        );
    }
}
