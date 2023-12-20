package com.lawman.gateway.authserver.exception.customer;

import com.lawman.gateway.authserver.exception.base.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {

  public CustomerNotFoundException() {
    setCode("com.gateway.server.exception.customer.CustomerNotFoundException");
  }
}
