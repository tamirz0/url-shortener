package com.mateo.urlshortener.model.entity;

import java.time.LocalDateTime;

public record ShortUrl
        (Long id,
         String url,
         String shortCode,
         LocalDateTime createdAt,
         LocalDateTime lastAccessedAt) {
}
