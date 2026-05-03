package com.app.daymoya.global.security.jwt;

import com.app.daymoya.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

  public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
  public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

  private static final String TOKEN_TYPE_CLAIM = "tokenType";
  private static final String ROLE_CLAIM = "role";

  private static final String ACCESS_TOKEN_TYPE = "ACCESS";
  private static final String REFRESH_TOKEN_TYPE = "REFRESH";
  private static final String ROLE_PREFIX = "ROLE_";

  private final JwtProperties jwtProperties;
  private final SecretKey secretKey;

  public JwtProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  /** =====================================
   * accessToken 관련
   ========================================= */
  public String generateAccessToken(Long userId, String role) {
    return generateToken(userId, ACCESS_TOKEN_TYPE, jwtProperties.getAccessTokenExpirationMs(), role);
  }

  public boolean validateAccessToken(String token) {
    return validateToken(token, ACCESS_TOKEN_TYPE);
  }

  public long getAccessTokenExpirationMs() {
    return jwtProperties.getAccessTokenExpirationMs();
  }

  /** =====================================
   * refreshToken 관련
   ========================================= */
  public String generateRefreshToken(Long userId) {
    return generateToken(userId, REFRESH_TOKEN_TYPE, jwtProperties.getRefreshTokenExpirationMs(), null);
  }

  public boolean validateRefreshToken(String token) {
    return validateToken(token, REFRESH_TOKEN_TYPE);
  }

  public long getRefreshTokenExpirationMs() {
    return jwtProperties.getRefreshTokenExpirationMs();
  }

  /** =====================================
   * token 공통
   ========================================= */
  public Long getUserId(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }

  public Authentication getAuthentication(String token) {

    Claims claims = getClaims(token);
    Long userId = Long.valueOf(claims.getSubject());
    String role = claims.get(ROLE_CLAIM, String.class);

    List<SimpleGrantedAuthority> authorities =
      role == null ? List.of() : List.of(new SimpleGrantedAuthority(ROLE_PREFIX + role));

    return new UsernamePasswordAuthenticationToken(userId, null, authorities);
  }

  private String generateToken(Long userId, String tokenType, long expirationMs, String role) {

    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirationMs);

    var builder = Jwts.builder()
      .subject(String.valueOf(userId))
      .claim(TOKEN_TYPE_CLAIM, tokenType)
      .issuedAt(now)
      .expiration(expiration)
      .signWith(secretKey);

    if (role != null) {
      builder.claim(ROLE_CLAIM, role);
    }

    return builder.compact();
  }

  private boolean validateToken(String token, String expectedTokenType) {
    try {
      String tokenType = getTokenType(token);
      return expectedTokenType.equals(tokenType);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private String getTokenType(String token) {
    return getClaims(token).get(TOKEN_TYPE_CLAIM, String.class);
  }

  private Claims getClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build()
      .parseSignedClaims(token).getPayload();
  }

}