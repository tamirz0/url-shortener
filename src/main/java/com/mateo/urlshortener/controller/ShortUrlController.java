package com.mateo.urlshortener.controller;

import com.mateo.urlshortener.model.dto.PagedResponse;
import com.mateo.urlshortener.model.entity.ShortUrl;
import com.mateo.urlshortener.service.ShortUrlService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/shorten")
public class ShortUrlController {

    ShortUrlService shortUrlService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ShortUrl> getShortUrl(@PathVariable String shortCode) {
        Optional<ShortUrl> shortUrl = shortUrlService.getShortUrl(shortCode);
        return shortUrl.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShortUrl> postShortUrl(@RequestBody String url, UriComponentsBuilder ucb) {
        try {
            ShortUrl shortUrl = shortUrlService.createShortUrl(url.replace('"', ' ').trim());
            URI location = ucb.
                    path("/shorten/{shortCode}")
                    .buildAndExpand(shortUrl.shortCode())
                    .toUri();
            return ResponseEntity.created(location).body(shortUrl);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Url already exists");
        }
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        try {
            shortUrlService.deleteShortUrl(shortCode);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ShortUrl>> getAllShortUrls(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        PagedResponse<ShortUrl> shortUrls = shortUrlService.getAllShortUrls(pageable);
        return ResponseEntity.ok(shortUrls);
    }
}
