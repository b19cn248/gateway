package com.lawman.gateway.authserver.service.impl;

import com.lawman.gateway.authserver.entity.Account;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.JwtTokenService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthConstant.CLAIMS;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthenticationConstant.CLAIM_AUTHORITIES_KEY;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.AuthenticationConstant.CLAIM_USERNAME_KEY;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

  @Value("${application.token.key}")
  private String key;
  @Value("${application.token.expire-time-access-token}")
  private long expireTimeAccessToken;
  @Value("${application.token.expire-time-refresh-token}")
  public long expireTimeRefreshToken;
  private final AccountService accountService;

  public String generateToken(String subject, Map<String, Object> claims, long tokeTimeLife) {
    log.info("(generateToken)start");
    return Jwts.builder()
          .setSubject(subject)
          .claim(CLAIMS, claims)
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + tokeTimeLife))
          .signWith(SignatureAlgorithm.HS256, key)
          .compact();
  }

  @Override
  public String generateAccessToken(Account account) {
    log.info("(generateAccessToken) account:{}", account);

    String role = accountService.getRole(account.getId());

    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_USERNAME_KEY, account.getUsername());
    claims.put(CLAIM_AUTHORITIES_KEY, role);

    return generateToken(account.getId(), claims, expireTimeAccessToken);
  }

  @Override
  public String generateRefreshToken(String userId, String username) {
    log.info("(generateRefreshToken)start");
    var claims = new HashMap<String, Object>();
    claims.put("username", username);
    return generateToken(userId, claims, expireTimeRefreshToken);
  }

  @Override
  public String getSubjectFromToken(String token) {
    validateToken(token);
    log.debug("(getSubjectFromToken)start");
    return getClaims(token, key).getSubject();
  }

  @Override
  public String getUsernameFromToken(String token) {
    validateToken(token);
    log.debug("(getUsernameFromToken) start");
    Map<String, Object> claims = getClaimsMap(token, key);
    return String.valueOf(claims.get("username"));
  }

  @Override
  public String getRoleFromToken(String token) {
    validateToken(token);
    log.debug("(getRoleFromToken) start");
    Map<String, Object> claims = getClaimsMap(token, key);
    String role = String.valueOf(claims.get("role"));
    log.info("(role) :{}", role);
    return role;
  }

  private Map<String, Object> getClaimsMap(String token, String key) {
    return (Map<String, Object>) getClaims(token, key).get(CLAIMS);
  }


  private Claims getClaims(String token, String secretKey) {
    log.debug("(getClaims)start");
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }


  private void validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    } catch (ExpiredJwtException ex) {
      log.error("JWT expired : {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      log.error("Token is null, empty or only whitespace : {}", ex.getMessage());
    } catch (MalformedJwtException ex) {
      log.error("JWT is invalid :{}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      log.error("JWT is not supported : {}", ex.getMessage());
    } catch (SignatureException ex) {
      log.error("Signature validation failed: {}", ex.getMessage());
    }

  }
}
