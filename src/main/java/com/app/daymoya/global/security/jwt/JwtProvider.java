package com.app.daymoya.global.security.jwt;

import com.app.daymoya.global.properties.JwtProperties;
import com.app.daymoya.global.constant.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static com.app.daymoya.global.constant.JwtConstants.EMAIL_CLAIM;

@Component
public class JwtProvider {

  private final JwtProperties jwtProperties;
  private final SecretKey secretKey;

  public JwtProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(Long memberId, String email) {

    Date now = new Date();
    Date expiration = new Date(now.getTime() + jwtProperties.getAccessTokenExpirationMs());

    return Jwts.builder()
      .subject(String.valueOf(memberId))
      .claim(EMAIL_CLAIM, email)
      .claim(JwtConstants.TOKEN_TYPE_CLAIM, JwtConstants.ACCESS_TOKEN_TYPE)
      .issuedAt(now)
      .expiration(expiration)
      .signWith(secretKey)
      .compact();
  }

  public String generateRefreshToken(Long memberId) {

    Date now = new Date();
    Date expiration = new Date(now.getTime() + jwtProperties.getRefreshTokenExpirationMs());

    return Jwts.builder()
      .subject(String.valueOf(memberId))
      .claim(JwtConstants.TOKEN_TYPE_CLAIM, JwtConstants.REFRESH_TOKEN_TYPE)
      .issuedAt(now)
      .expiration(expiration)
      .signWith(secretKey)
      .compact();
  }

  public boolean validateAccessToken(String token) {
    try {
      String tokenType = getTokenType(token);
      return JwtConstants.ACCESS_TOKEN_TYPE.equals(tokenType);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public boolean validateRefreshToken(String token) {
    try {
      String tokenType = getTokenType(token);
      return JwtConstants.REFRESH_TOKEN_TYPE.equals(tokenType);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public Long getMemberId(String token) {
    Claims claims = getClaims(token);
    return Long.valueOf(claims.getSubject());
  }

  public String getEmail(String token) {
    Claims claims = getClaims(token);
    return claims.get(EMAIL_CLAIM, String.class);
  }

  public String getTokenType(String token) {
    Claims claims = getClaims(token);
    return claims.get(JwtConstants.TOKEN_TYPE_CLAIM, String.class);
  }

  public Authentication getAuthentication(String token) {

    Claims claims = getClaims(token);
    Long memberId = Long.valueOf(claims.getSubject());

    return new UsernamePasswordAuthenticationToken(memberId, null, List.of());
  }

  public long getAccessTokenExpirationMs() {
    return jwtProperties.getAccessTokenExpirationMs();
  }

  public long getRefreshTokenExpirationMs(){
    return jwtProperties.getRefreshTokenExpirationMs();
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

}
