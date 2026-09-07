package com.mateo.urlshortener.service;

import com.mateo.urlshortener.mapper.PagedResponseMapper;
import com.mateo.urlshortener.model.dto.PagedResponse;
import com.mateo.urlshortener.model.entity.ShortUrl;
import com.mateo.urlshortener.repository.ShortUrlRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    private final PagedResponseMapper pagedResponseMapper;

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, PagedResponseMapper pagedResponseMapper) {
        this.shortUrlRepository = shortUrlRepository;
        this.pagedResponseMapper = pagedResponseMapper;
    }

    @Override
    public Optional<ShortUrl> getShortUrl(String shortCode) {
        return shortUrlRepository.findByShortCode(shortCode);
    }

    @Override
    public ShortUrl createShortUrl(String url) {
        String cleanedUrl = cleanUrl(url);
        String shortCode = generateShortCode(cleanedUrl);
        ShortUrl shortUrl = new ShortUrl(null, cleanedUrl, shortCode, LocalDateTime.now(), null);

        return shortUrlRepository.save(shortUrl);
    }

    @Override
    public void deleteShortUrl(String shortCode) {
        Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByShortCode(shortCode);
        if (shortUrlOptional.isPresent()) {
            shortUrlRepository.delete(shortUrlOptional.get());
        } else {
            throw new IllegalArgumentException("Short URL not found");
        }
    }

    @Override
    public PagedResponse<ShortUrl> getAllShortUrls(Pageable pageable) {
        Page<ShortUrl> page = shortUrlRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(page);
    }

    //TODO
    private String cleanUrl(String url) {
        if (url.equals(" ")) {
            throw new IllegalArgumentException("Invalid Url");
        }
        return url.trim();
    }

    //TODO
    private String generateShortCode(String url) {
        return String.valueOf(url.hashCode());
    }
}
