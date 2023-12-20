package com.lawman.gateway.authserver.exception.account;


import com.lawman.gateway.authserver.exception.base.BadRequestException;

public class AccountHasBeenLockedException extends BadRequestException {

  public AccountHasBeenLockedException() {
    setCode("com.gateway.server.exception.account.AccountHasBeenLockedException");
  }
}
