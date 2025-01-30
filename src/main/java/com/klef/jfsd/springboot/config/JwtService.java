package com.klef.jfsd.springboot.config;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
/**
 * The JwtService class provides methods for generating, validating, and extracting information from JWT tokens in a Java application.
 */
public class JwtService {
	
	private static final String SECRET_KEY = "ogI+rGjo2q4IPEtiBD3dPC71SISgK6pod1dh3H0tP3I="; // Replace with your actual secret key
	
	private Key getSignInKey() {
		byte[] keybytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keybytes);
	}

    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim from JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate a new token
	public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*10*10*24))//Expiration Set to 24 Hrs.
                .signWith(getSignInKey())
                .compact();
    }

    // Validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
//        System.out.println(username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts
        		.parser()
        		.verifyWith((SecretKey)getSignInKey())
        		.build()
        		.parseSignedClaims(token)
        		.getPayload();
    }

}
