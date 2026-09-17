package com.mateo.urlshortener.util;

import java.math.BigInteger;

class Base62Encoder {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    String toBase62(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        BigInteger number = new BigInteger(1, bytes);
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
