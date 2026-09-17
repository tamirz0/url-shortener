package com.mateo.urlshortener.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class Base62TokenGeneratorTest {
    static Base62TokenGenerator generator;

    @BeforeAll
    static void setUp() {
        generator = new Base62TokenGenerator();
    }

    @Test
    void generate_millionTokens_generatesUniqueAndNotEmptyTokens() {
        final long TOKENS_TO_GENERATE = 1_000_000L;
        Set<String> tokens = new java.util.HashSet<>();
        for (long i = 0; i < TOKENS_TO_GENERATE; i++) {
            String token = generator.generate();
            if (token.isEmpty()) {
                Assertions.fail("Empty token generated");
            }
            if (tokens.contains(token)) {
                Assertions.fail("Duplicate token generated: " + token);
            }

            tokens.add(token);
        }
        Assertions.assertEquals(TOKENS_TO_GENERATE, tokens.size());
    }

    @Test
    void generate_thousandTokens_generatesTokensOfBase62Characters() {
        final long TOKENS_TO_GENERATE = 10_000L;
        for (long i = 0; i < TOKENS_TO_GENERATE; i++) {
            String token = generator.generate();
            Assertions.assertTrue(token.matches("^[0-9a-zA-Z]+$"), "Token contains invalid characters: " + token);
        }
    }
}