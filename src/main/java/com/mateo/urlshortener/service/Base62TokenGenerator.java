package com.mateo.urlshortener.service;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
class Base62TokenGenerator implements TokenGenerator {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_LENGTH = 12;

    @Override
    public String generate() {
        byte[] randomBytes = getRandomBytes();
        return toBase62(randomBytes);
    }

    private byte[] getRandomBytes() {
        byte[] bytes = new byte[Base62TokenGenerator.TOKEN_LENGTH];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    private String toBase62(byte[] bytes) {
        BigInteger number = new BigInteger(bytes);
        StringBuilder sb = new StringBuilder();
        BigInteger base = BigInteger.valueOf(BASE62.length());

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = number.divideAndRemainder(base);
            sb.append(BASE62.charAt(divmod[1].intValue()));
            number = divmod[0];
        }
        return sb.reverse().toString(); // Mas eficiente que estar insertando al principio cada caracter
    }
}
