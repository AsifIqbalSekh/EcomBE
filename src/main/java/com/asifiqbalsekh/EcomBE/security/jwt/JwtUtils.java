package com.asifiqbalsekh.EcomBE.security.jwt;

import com.asifiqbalsekh.EcomBE.security.securityservice.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.secret.expiresDuration}")
    private int jwtExpiresMs;

    @Value("${jwt.secret.cookie}")
    private String jwtCookie;

    //Parsing......

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken= request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public String getUsernameFromJwt(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //Validation....

    public boolean validateJwt(String token) {

        try {
            log.info("Validating token ..");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
            return true;
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }


    //Generating.....


    public String generateTokenFromUsername(UserDetails userDetails) {
        String username=userDetails.getUsername();
        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+jwtExpiresMs))
                .signWith(key())
                .compact();
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //For JWT Cookie based Authentication

//    public String getJwtFromCookie(HttpServletRequest request) {
//        Cookie cookie= WebUtils.getCookie(request,jwtCookie);
//        if(cookie != null) {
//            return cookie.getValue();
//        }
//        return null;
//    }
//
//    public ResponseCookie generateJwtCookie(UserDetailsImpl user) {
//
//        String jwt = generateTokenFromUsername(user);
//        return ResponseCookie.from(jwtCookie, jwt)
//                .path("/api")
//                .maxAge(jwtExpiresMs)
//                .httpOnly(false)
//                .build();
//    }
//
//    public ResponseCookie generateJwtCleanCookie() {
//        return ResponseCookie.from(jwtCookie,null)
//                .path("/api")
//                .build();
//    }


}
