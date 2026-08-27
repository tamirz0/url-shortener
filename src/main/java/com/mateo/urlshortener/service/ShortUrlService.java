package com.mateo.urlshortener.service;

import com.mateo.urlshortener.model.entity.ShortUrl;

import java.util.Optional;

public interface ShortUrlService {
    Optional<ShortUrl> getShortUrl(String shortCode);
}
