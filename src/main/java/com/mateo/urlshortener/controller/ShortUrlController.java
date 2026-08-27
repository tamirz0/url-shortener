package com.mateo.urlshortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
class ShortUrlController {

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getShortUrl(@PathVariable String shortUrl) {
        return ResponseEntity.ok("Ok " + shortUrl);
    }
}
