package com.lawman.gateway.authserver.exception.seller;


import com.lawman.gateway.authserver.exception.base.NotFoundException;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.ExceptionMessage.SELLER_PHONE_NUMBER_NOT_FOUND_EXCEPTION;

public class SellerPhoneNumberNotFoundException extends NotFoundException {

  public SellerPhoneNumberNotFoundException() {
    setCode(SELLER_PHONE_NUMBER_NOT_FOUND_EXCEPTION);
  }
}
