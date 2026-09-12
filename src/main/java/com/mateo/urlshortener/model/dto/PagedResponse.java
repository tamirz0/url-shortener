package com.mateo.urlshortener.model.dto;

import java.util.List;

public record PagedResponse<T>(Meta meta, List<T> data) {
    public record Meta(
            int page,
            int size,
            int totalPages,
            long totalElements
    ) {
    }
}

