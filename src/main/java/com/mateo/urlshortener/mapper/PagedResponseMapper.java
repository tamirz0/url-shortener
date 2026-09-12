package com.mateo.urlshortener.mapper;

import com.mateo.urlshortener.model.dto.PagedResponse;
import org.springframework.data.domain.Page;

public final class PagedResponseMapper {
    private PagedResponseMapper() {
    }

    public static <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return new PagedResponse<>(
                new PagedResponse.Meta(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalPages(),
                        page.getTotalElements()
                ),
                page.getContent()
        );
    }
}
