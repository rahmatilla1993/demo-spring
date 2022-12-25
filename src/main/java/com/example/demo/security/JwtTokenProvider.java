package com.example.demo.security;

import com.example.demo.exceptions.JwtAuthenticationException;
import com.example.demo.services.PersonDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtTokenProvider {

    @Value(value = "${jwt.secretKey}")
    private String secretKey;

    private final PersonDetailsService personDetailsService;

    @Autowired
    public JwtTokenProvider(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    public String createToken(String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .after(new Date());
        }
        catch (Exception e) {
            throw new JwtAuthenticationException("Token is expired or invalid", UNAUTHORIZED);
        }
    }

    public Authentication authentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = personDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest servletRequest) {
        return servletRequest.getHeader("Authorization");
    }

    public String getUsername(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
