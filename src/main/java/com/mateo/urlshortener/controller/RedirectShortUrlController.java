package com.mateo.urlshortener.controller;

import com.mateo.urlshortener.model.entity.ShortUrl;
import com.mateo.urlshortener.service.ShortUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
class RedirectShortUrlController {

    ShortUrlService shortUrlService;

    public RedirectShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("{shortCode}")
    public ResponseEntity<Void> redirectShortUrl(@PathVariable String shortCode) {
        Optional<ShortUrl> shortUrl = shortUrlService.getShortUrl(shortCode);
        return shortUrl.<ResponseEntity<Void>>map(url -> ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", url.url())
                .build()).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
