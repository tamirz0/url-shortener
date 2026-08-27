package com.mateo.urlshortener.controller;

import com.mateo.urlshortener.model.entity.ShortUrl;
import com.mateo.urlshortener.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ShortUrlController.class)
public class ShortUrlControllerTest {

    @Autowired
    private MockMvc mvc;

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
}
