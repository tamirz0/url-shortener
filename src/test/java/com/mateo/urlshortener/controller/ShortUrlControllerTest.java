package com.mateo.urlshortener.controller;

import com.mateo.urlshortener.model.dto.PagedResponse;
import com.mateo.urlshortener.model.entity.ShortUrl;
import com.mateo.urlshortener.service.ShortUrlService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ShortUrlController.class)
public class ShortUrlControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private ShortUrlService shortUrlService;

    @Test
    public void getShortUrl_whenExists_returnShortUrl() throws Exception {
        Mockito.when(shortUrlService.getShortUrl("abc123"))
                .thenReturn
                        (Optional.of(new ShortUrl(
                                1L,
                                "https://example.com",
                                "abc123",
                                null,
                                null)));

        mvc.perform(get("/shorten/abc123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").value("abc123"));
    }

    @Test
    public void getShortUrl_whenNotExists_returnNotFound() throws Exception {
        Mockito.when(shortUrlService.getShortUrl("xyz789")).thenReturn(Optional.empty());

        mvc.perform(get("/shorten/xyz789").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postShortUrl_whenValidUrl_returnCreated() throws Exception {
        String url = "https://google.com";
        String expectedLocation = "/shorten/gog999";
        Mockito.when(shortUrlService.createShortUrl(url)).thenReturn(new ShortUrl(
                1L,
                url,
                "gog999",
                null,
                null));

        mvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString(expectedLocation)))
                .andExpect(jsonPath("$.url", Matchers.is(url)))
                .andExpect(jsonPath("$.shortCode", Matchers.is("gog999")));
    }

    @Test
    public void postShortUrl_whenInvalidUrl_returnBadRequest() throws Exception {
        String invalidUrl = "invalid-url";
        Mockito.when(shortUrlService.createShortUrl(invalidUrl)).thenThrow(new IllegalArgumentException("Invalid URL"));

        mvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUrl)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Invalid URL"));
    }

    @Test
    public void postShortUrl_whenUrlAlreadyExists_returnConflict() throws Exception {
        String url = "https://google.com";
        Mockito.when(shortUrlService.createShortUrl(url)).thenThrow(new DuplicateKeyException("Url already exists"));

        mvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isConflict())
                .andExpect(status().reason("Url already exists"));
    }

    @Test
    public void deleteShortUrl_whenExists_returnNoContent() throws Exception {
        String shortCode = "abc123";
        mvc.perform(delete("/shorten/" + shortCode))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void deleteShortUrl_whenNotExists_returnNotFound() throws Exception {
        String shortCode = "xyz789";
        Mockito.doThrow(new IllegalArgumentException("Url not found")).when(shortUrlService).deleteShortUrl(shortCode);

        mvc.perform(delete("/shorten/" + shortCode))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Url not found"));
    }

    @Test
    public void getListOfShortUrls_withQueryParameters_returnSortedAndPagedList() throws Exception {
        List<ShortUrl> shortUrls = List.of(
                new ShortUrl(3L, "https://www.example.com/another/long/url", "ghi789", LocalDateTime.of(2004, 2, 10, 15, 30), null),
                new ShortUrl(2L, "https://www.example.com/some/long/url", "def456", LocalDateTime.of(2026, 9, 2, 10, 0), null),
                new ShortUrl(1L, "https://google.com", "abc123", LocalDateTime.of(2026, 9, 3, 12, 0), null)
        );
        PagedResponse<ShortUrl> ascResponse = new PagedResponse<>(new PagedResponse.meta(1, 2, 25, 50), shortUrls);
        PagedResponse<ShortUrl> descResponse = new PagedResponse<>(new PagedResponse.meta(1, 2, 25, 50), shortUrls.reversed());

        Mockito.when(shortUrlService.getAllShortUrls(PageRequest.of(1, 2, Sort.by("createdAt").ascending())))
                .thenReturn(ascResponse);

        Mockito.when(shortUrlService.getAllShortUrls(PageRequest.of(1, 2, Sort.by("createdAt").descending())))
                .thenReturn(descResponse);

        ResultActions resultActions = mvc.perform(get("/shorten?page=1&size=2&sort=createdAt,asc"));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.size").value(2))
                .andExpect(jsonPath("$.meta.totalPages").value(25))
                .andExpect(jsonPath("$.meta.totalElements").value(50))
                .andExpect(jsonPath("$.data", Matchers.hasSize(shortUrls.size())))
                .andExpect(jsonPath("$.data[*].id", Matchers.contains(3, 2, 1)));

        mvc.perform(get("/shorten?page=1&size=2&sort=createdAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.size").value(2))
                .andExpect(jsonPath("$.meta.totalPages").value(25))
                .andExpect(jsonPath("$.meta.totalElements").value(50))
                .andExpect(jsonPath("$.data", Matchers.hasSize(shortUrls.size())))
                .andExpect(jsonPath("$.data[*].id", Matchers.contains(1, 2, 3)));
    }
}
