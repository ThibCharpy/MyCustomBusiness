package com.dev.mcb.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JJWTUtil {

    private static final String DEFAULT_ISSUER = "MyCustomBusiness";

    public static String issueToken(String login, String key) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuer(DEFAULT_ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(60L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    /**
     * Convert a {@link LocalDateTime} to a {@link Date} object
     * @param localDateTime the LocalDateTime to convert
     * @return the date object
     */
    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
