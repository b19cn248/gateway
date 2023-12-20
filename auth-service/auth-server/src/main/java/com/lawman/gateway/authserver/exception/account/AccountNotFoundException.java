package com.lawman.gateway.authserver.exception.account;


import com.lawman.gateway.authserver.constanst.GatewayServerConstants;
import com.lawman.gateway.authserver.exception.base.NotFoundException;

public class AccountNotFoundException extends NotFoundException {

  public AccountNotFoundException() {
    setCode(GatewayServerConstants.ExceptionMessage.ACCOUNT_NOT_FOUND_EXCEPTION);
  }
}
