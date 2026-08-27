package com.mateo.urlshortener.model.entity;

import java.time.LocalDateTime;

public record ShortUrl
        (long id,
         String url,
         String shortCode,
         LocalDateTime createdAt,
         LocalDateTime lastAccessedAt) {
}
