package com.lawman.gateway.authserver.exception.customer;


import com.lawman.gateway.authserver.exception.base.ConflictException;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.ExceptionMessage.PHONE_NUMBER_ALREADY_EXISTED_EXCEPTION;

public class PhoneNumberAlreadyExistedException extends ConflictException {

  public PhoneNumberAlreadyExistedException() {
    setCode(PHONE_NUMBER_ALREADY_EXISTED_EXCEPTION);
  }
}
