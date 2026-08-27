package com.mateo.urlshortener.controller;

import com.mateo.urlshortener.model.entity.ShortUrl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
class ShortUrlController {

    @GetMapping("/{shortCode}")
    public ResponseEntity<ShortUrl> getShortUrl(@PathVariable String shortCode) {
        if (!shortCode.equalsIgnoreCase("abc123")) {
            return ResponseEntity.notFound().build();
        }
        ShortUrl url = new ShortUrl(1L, "google.com", shortCode, null, null);
        return ResponseEntity.ok(url);
    }
}
