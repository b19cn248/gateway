package com.lawman.gateway.authserver.exception.auth;


import com.lawman.gateway.authserver.exception.base.BadRequestException;

public class TokenInvalidException extends BadRequestException {

  public TokenInvalidException() {
    setCode("com.gateway.server.exception.auth.TokenInvalidException");
  }
}
