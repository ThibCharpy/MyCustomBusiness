package com.dev.mcb.util.auth;

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

    public static boolean verifyToken(String login, String key, String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals(login);
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
