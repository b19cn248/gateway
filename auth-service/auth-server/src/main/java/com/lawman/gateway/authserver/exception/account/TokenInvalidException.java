package com.lawman.gateway.authserver.exception.account;


import com.lawman.gateway.authserver.exception.base.BadRequestException;

public class TokenInvalidException extends BadRequestException {

  public TokenInvalidException() {
    setCode("com.gateway.server.exception.account.TokenInvalidException");
  }
}
