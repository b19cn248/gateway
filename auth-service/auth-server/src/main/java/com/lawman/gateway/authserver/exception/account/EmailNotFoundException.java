package com.lawman.gateway.authserver.exception.account;


import com.lawman.gateway.authserver.exception.base.BadRequestException;

public class EmailNotFoundException extends BadRequestException {

  public EmailNotFoundException() {
    setCode("com.gateway.server.exception.account.EmailNotFoundException");
  }
}
