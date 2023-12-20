package com.lawman.gateway.authserver.exception.account;


import com.lawman.gateway.authserver.exception.base.BadRequestException;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.ExceptionMessage.PASSWORD_INCORRECT_EXCEPTION;

public class PasswordIncorrectException extends BadRequestException {
  public PasswordIncorrectException() {
    setCode(PASSWORD_INCORRECT_EXCEPTION);
  }
}
