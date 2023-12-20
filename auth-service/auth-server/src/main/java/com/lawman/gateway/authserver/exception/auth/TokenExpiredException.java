package com.lawman.gateway.authserver.exception.auth;


import com.lawman.gateway.authserver.exception.base.BadRequestException;

public class TokenExpiredException extends BadRequestException {
  public TokenExpiredException() {
    setCode("com.gateway.server.exception.auth.TokenExpiredException");
  }
}
