package com.deposit.apigateway.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JwtService{

  @Value("${jwt.signing.key}")
  private String jwtSigningKey;

  @Value("${jwt.expiry.duration:1h}")
  private Duration tokenDuration;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }


  public String generateToken(String username) {
    Date issuedAt = new Date(System.currentTimeMillis());
    Date expiration = new Date(issuedAt.getTime() + tokenDuration.toMillis());

    return Jwts.builder()
        .setIssuedAt(issuedAt)
        .setExpiration(expiration)
        .setSubject(username)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    return (extractUsername(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
        .getBody();
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    final Claims claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}