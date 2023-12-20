package com.lawman.gateway.authserver.exception.customer;


import com.lawman.gateway.authserver.exception.base.ConflictException;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.ExceptionMessage.EMAIL_ALREADY_EXISTED_EXCEPTION;

public class EmailAlreadyExistedException extends ConflictException {

  public EmailAlreadyExistedException() {
    setCode(EMAIL_ALREADY_EXISTED_EXCEPTION);
  }
}
