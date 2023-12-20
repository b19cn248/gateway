package com.lawman.gateway.authserver.exception.account;

import com.lawman.gateway.authserver.exception.base.NotFoundException;

public class UsernameNotFoundException extends NotFoundException {
  public UsernameNotFoundException() {
    setCode("com.gateway.server.exception.account.UsernameNotFoundException");
  }
}
