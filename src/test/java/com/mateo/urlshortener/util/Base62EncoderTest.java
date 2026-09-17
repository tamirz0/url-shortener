package com.mateo.urlshortener.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Base62EncoderTest {
    private static Base62Encoder encoder;

    @BeforeAll
    static void setup() {
        encoder = new Base62Encoder();
    }

    @Test
    void toBase62_simpleTest() {
        String expected = "TRblG";
        byte[] input = "1234".getBytes();
        String actual = encoder.toBase62(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toBase62_emptyInput() {
        String expected = "";
        byte[] input = new byte[0];
        String actual = encoder.toBase62(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toBase62_nullInput() {
        String expected = "";
        byte[] input = null;
        String actual = encoder.toBase62(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toBase62_largeInput() {
        String expected = "EGPitFZO2NKwssBucp3DJdtnvM2FXrkkuuiQI5wrNcNDUZ70kh5GITwRHt74zrxq3wue5P6TBh3rfqOdW30";
        byte[] input = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
        String actual = encoder.toBase62(input);
        Assertions.assertEquals(expected, actual);
    }
}