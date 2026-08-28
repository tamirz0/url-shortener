package com.mateo.urlshortener.controller;

import com.mateo.urlshortener.model.entity.ShortUrl;
import com.mateo.urlshortener.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RedirectShortUrlController.class)
class RedirectShortUrlControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ShortUrlService shortUrlService;

    @Test
    public void redirectShortUrl_whenExists_returnMovedPermanently() throws Exception {
        String shortCode = "abc123";
        ShortUrl shortUrl = new ShortUrl(1L, "https://google.com", shortCode, null, null);
        Mockito.when(shortUrlService.getShortUrl(shortCode)).thenReturn(Optional.of(shortUrl));

        mvc.perform(get("/" + shortCode))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", shortUrl.url()));
    }

    @Test
    public void redirectShortUrl_whenNotExists_returnNotFound() throws Exception {
        String shortCode = "xyz789";
        Mockito.when(shortUrlService.getShortUrl(shortCode)).thenReturn(Optional.empty());

        mvc.perform(get("/" + shortCode))
                .andExpect(status().isNotFound());
    }
}
