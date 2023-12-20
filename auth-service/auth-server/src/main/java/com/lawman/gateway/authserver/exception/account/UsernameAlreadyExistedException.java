package com.lawman.gateway.authserver.exception.account;

import com.lawman.gateway.authserver.exception.base.ConflictException;

public class UsernameAlreadyExistedException extends ConflictException {

  public UsernameAlreadyExistedException() {
    setCode("com.gateway.server.exception.base.account.UsernameAlreadyExistedException");
  }
}
