package com.mateo.urlshortener.model.dto;

import java.util.List;

public record PagedResponse<T>(PagedResponse.meta meta, List<T> data) {
    public record meta(
            int page,
            int size,
            int totalPages,
            int totalElements
    ) {
    }
}

