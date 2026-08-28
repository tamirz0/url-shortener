package com.mateo.urlshortener.controller;

import com.mateo.urlshortener.model.entity.ShortUrl;
import com.mateo.urlshortener.service.ShortUrlService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
