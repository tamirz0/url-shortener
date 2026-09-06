package com.mateo.urlshortener.service;

import com.mateo.urlshortener.model.dto.PagedResponse;
import com.mateo.urlshortener.model.entity.ShortUrl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ShortUrlService {
    Optional<ShortUrl> getShortUrl(String shortCode);

    ShortUrl createShortUrl(String url);

    void deleteShortUrl(String shortCode);

    PagedResponse<ShortUrl> getAllShortUrls(Pageable pageable);
}
