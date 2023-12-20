package com.lawman.gateway.authserver.service;


import com.lawman.gateway.authserver.entity.Account;

public interface JwtTokenService {
  String generateAccessToken(Account account);

  String generateRefreshToken(String userId, String username);

  String getSubjectFromToken(String token);

  String getUsernameFromToken(String token);

  String getRoleFromToken(String token);
}
