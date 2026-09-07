package com.mateo.urlshortener.repository;

import com.mateo.urlshortener.model.entity.ShortUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends CrudRepository<ShortUrl, Long>, PagingAndSortingRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);
}
